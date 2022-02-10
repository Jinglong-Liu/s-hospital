package com.exp.hospital.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exp.hospital.cmn.mapper.DictMapper;
import com.exp.hospital.cmn.service.DictService;
import com.exp.hospital.model.cmn.Dict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
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

    //判断id是否有子节点
    private boolean hasChildren(Long id){
        QueryWrapper<Dict>wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

}
