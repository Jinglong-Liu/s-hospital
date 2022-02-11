package com.exp.hospital.hosp.controller.api;

import com.exp.hospital.common.exception.HospitalException;
import com.exp.hospital.common.helper.HttpRequestHelper;
import com.exp.hospital.common.result.Result;
import com.exp.hospital.common.result.ResultCodeEnum;
import com.exp.hospital.common.util.MD5;
import com.exp.hospital.hosp.service.DepartmentService;
import com.exp.hospital.hosp.service.HospitalService;
import com.exp.hospital.hosp.service.HospitalSetService;
import com.exp.hospital.hosp.service.ScheduleService;
import com.exp.hospital.model.hosp.Department;
import com.exp.hospital.model.hosp.Hospital;
import com.exp.hospital.model.hosp.Schedule;
import com.exp.hospital.vo.hosp.DepartmentQueryVo;
import com.exp.hospital.vo.hosp.ScheduleQueryVo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ScheduleService scheduleService;
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
        checkSign(paramMap);
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

        checkSign(paramMap);

        departmentService.save(paramMap);
        return Result.ok();
    }

    // 查询科室
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        // 获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取医院编号
        //医院编号
        String hoscode = (String)paramMap.get("hoscode");

        //当前页 和 每页记录数
        int pageNum = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String)paramMap.get("limit"));

        // TODO:签名校验
        checkSign(paramMap);

        //
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service
        Page<Department> pageModel = departmentService.findPageDepartment(pageNum,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }
    private void checkSign( Map<String, Object> paramMap){
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
    }
    // 删除科室
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        checkSign(paramMap);
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        checkSign(paramMap);
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }
    // 上传排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        checkSign(paramMap);
        scheduleService.save(paramMap);
        return Result.ok();
    }
    // 查询排班接口
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //医院编号
        String hoscode = (String)paramMap.get("hoscode");

        //科室编号
        String depcode = (String)paramMap.get("depcode");
        //当前页 和 每页记录数
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String)paramMap.get("limit"));
        //TODO 签名校验
        checkSign(paramMap);
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        //调用service方法
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //删除排班
    @PostMapping("schedule/remove")
    public Result remove(HttpServletRequest request) {
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取医院编号和排班编号
        String hoscode = (String)paramMap.get("hoscode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");

        //TODO 签名校验
        checkSign(paramMap);
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }
}