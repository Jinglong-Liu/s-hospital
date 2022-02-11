package com.exp.hospital.hosp.controller;

import com.exp.hospital.common.result.Result;
import com.exp.hospital.hosp.service.HospitalService;
import com.exp.hospital.model.hosp.Hospital;
import com.exp.hospital.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("医院列表(分页+条件查询)")
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo){
        // 从 mongodb 查询
        Page<Hospital> pageModel = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("更新医院的上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable("id") String id,
                                   @PathVariable("status") Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //查询医院详情
    @ApiOperation("查询医院详情")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable("id") String id){
        //为了方便后续取值方便，此处查询不用Hospital接受，而是放在集合里面
        Map<String, Object> map = hospitalService.getHospById(id);
        return Result.ok(map);
    }
}
