package com.exp.hospital.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exp.hospital.cmn.listener.DictListener;
import com.exp.hospital.cmn.mapper.DictMapper;
import com.exp.hospital.cmn.service.DictService;
import com.exp.hospital.model.cmn.Dict;
import com.exp.hospital.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict>wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //list集合中每个list对象的hasChildren
        //循环查询数据库好像不合适
        dictList.forEach(dict -> {
            Long dictId = dict.getId();
            boolean hasChild = hasChildren(dictId);
            dict.setHasChildren(hasChild);
        });
        return dictList;
    }

    /**
     * 下载字典
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        //查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict -- DictEeVo
        List<DictEeVo> dictVoList = new ArrayList<>();
        for(Dict dict:dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            // dictEeVo.setId(dict.getId());
            BeanUtils.copyProperties(dict,dictEeVo);//get出来再set进入
            dictVoList.add(dictEeVo);
        }
        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入数据字典
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        // 如果dictCode 为空,直接根据value查询
        if(StringUtils.isEmpty(dictCode)) {
            QueryWrapper<Dict>wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            //System.out.println(dictCode);
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parentId = codeDict.getId();
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parentId)
                    .eq("value", value));
            return dict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        // 根据dictCode 获取id,再根据id查下层结点
        Dict dict = this.getDictByDictCode(dictCode);
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict>wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }
    //判断id是否有子节点
    private boolean hasChildren(Long id){
        QueryWrapper<Dict>wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

}
