package com.exp.hospital.hosp.controller.api;

import com.exp.hospital.common.exception.HospitalException;
import com.exp.hospital.common.helper.HttpRequestHelper;
import com.exp.hospital.common.result.Result;
import com.exp.hospital.common.result.ResultCodeEnum;
import com.exp.hospital.common.util.MD5;
import com.exp.hospital.hosp.service.DepartmentService;
import com.exp.hospital.hosp.service.HospitalService;
import com.exp.hospital.hosp.service.HospitalSetService;
import com.exp.hospital.model.hosp.Hospital;
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
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    //1、上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        // 获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取签名（加密的）
        String hospSign = (String)paramMap.get("sign");
        // 查询数据库对比，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        // 加密数据库查到的密码对比
        String signKeyMd5 = MD5.encrypt(signKey);
        // 判断是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        // 图片处理：+ 号会变成空格,需要把空格转+
        String logoData = ((String) paramMap.get("logoData")).replaceAll(" ","+");
        paramMap.put("logoData",logoData);
        hospitalService.save(paramMap);
        return Result.ok();
    }

    //2. 查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取编号
        String hoscode = (String) paramMap.get("hoscode");
        // 获取签名（加密的）
        String hospSign = (String)paramMap.get("sign");
        // 查询数据库对比，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        // 加密数据库查到的密码对比
        String signKeyMd5 = MD5.encrypt(signKey);
        // 判断是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        // 调用service 查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    // 上传科室
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        // 获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);


        // 获取签名（加密的）
        String hospSign = (String)paramMap.get("sign");
        // 查询数据库对比，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        // 加密数据库查到的密码对比
        String signKeyMd5 = MD5.encrypt(signKey);
        // 判断是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(paramMap);
        return Result.ok();
    }
}