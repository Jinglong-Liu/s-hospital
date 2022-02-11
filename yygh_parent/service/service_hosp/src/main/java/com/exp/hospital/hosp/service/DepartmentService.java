package com.exp.hospital.hosp.service;


import com.exp.hospital.model.hosp.Department;
import com.exp.hospital.vo.hosp.DepartmentQueryVo;
import com.exp.hospital.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    // 查询科室接口
    Page<Department> findPageDepartment(int pageNum, int limit, DepartmentQueryVo departmentQueryVo);

    // 删除科室接口
    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);
}
