package com.exp.hospital.hosp.controller.api;

import com.exp.hospital.common.exception.HospitalException;
import com.exp.hospital.common.helper.HttpRequestHelper;
import com.exp.hospital.common.result.Result;
import com.exp.hospital.common.result.ResultCodeEnum;
import com.exp.hospital.common.util.MD5;
import com.exp.hospital.hosp.service.HospitalService;
import com.exp.hospital.hosp.service.HospitalSetService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    //1、上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        // 获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        hospitalService.save(paramMap);
        return Result.ok();
    }
}