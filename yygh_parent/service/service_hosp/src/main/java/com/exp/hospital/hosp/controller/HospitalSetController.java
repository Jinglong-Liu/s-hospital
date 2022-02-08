package com.exp.hospital.hosp.controller;

import com.exp.hospital.hosp.service.HospitalSetService;
import com.exp.hospital.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //http://localhost:8201/admin/hosp/hospitalSet/findAll
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        //return Result.ok(list);
        return list;
    }

    //2 逻辑删除医院设置
    @DeleteMapping("{id}")
    public boolean removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag;
    }
}
