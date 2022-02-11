package com.exp.hospital.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.exp.hospital.cmn.client.DictFeignClient;
import com.exp.hospital.common.result.Result;
import com.exp.hospital.hosp.repository.HospitalRepository;
import com.exp.hospital.hosp.service.HospitalService;
import com.exp.hospital.model.hosp.Hospital;
import com.exp.hospital.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;
    @Override
    public void save(Map<String, Object> paramMap) {


        // 转换成对象
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        // 判断是否存在相同的数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        // 不存在，进行添加；存在，修改更新
        //如果存在，进行修改
        if(hospitalExist != null) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {//如果不存在，进行添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    /**
     * 根据编号查询医院
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // mongodb
        // 条件查询
        Pageable pageable = PageRequest.of(page - 1,limit);
        // 构建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        // 转换成hospital
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        // 创建example 对象
        Example<Hospital>example = Example.of(hospital,matcher);
        // 调用repository
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //遍历进行医院等级封装 远程调用
        pages.getContent().stream().forEach(item->{
            this.selectHospitalHosType(item);
        });
        return pages;

    }

    private Hospital selectHospitalHosType(Hospital hospital) {
        Result result = dictFeignClient.getName("Hostype", hospital.getHostype());
        String hostypeString = (String) result.getData();
        String provinceString = (String) dictFeignClient.getName(hospital.getProvinceCode()).getData();
        String cityString = (String) dictFeignClient.getName(hospital.getCityCode()).getData();
        String districtString = (String) dictFeignClient.getName(hospital.getDistrictCode()).getData();
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString + cityString +districtString);
        return hospital;
    }
}
