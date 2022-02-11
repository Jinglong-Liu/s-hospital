package com.exp.hospital.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.exp.hospital.hosp.repository.DepartmentRepository;
import com.exp.hospital.hosp.service.DepartmentService;
import com.exp.hospital.model.hosp.Department;
import com.exp.hospital.vo.hosp.DepartmentQueryVo;
import com.exp.hospital.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //map 转成department对象
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString,Department.class);

        // 是否存在  根据医院编号和科室编号查询
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        if(departmentExist!=null) {
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(int pageNum, int limit, DepartmentQueryVo departmentQueryVo) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);//赋值
        department.setIsDeleted(0);
        // 创建 pageable对象，设置page和limit
        // 从0开始
        Pageable pageable = PageRequest.of(pageNum-1,limit);
        // 创建 example对象
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department,matcher);

        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        // 先根据医院编号和科室编号查询信息
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null){
            //删除
            departmentRepository.deleteById(department.getId());
        }

    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建List集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();
        //根据医院编号，查询所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        //所有科室列表信息
        List<Department> departmentList = departmentRepository.findAll(example);

        //根据大科室编号bigcode进行分组，获取每个大科室里面的下级子科室
        Map<String, List<Department>> departmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map集合
        for(Map.Entry<String,List<Department>> entry : departmentMap.entrySet()){
            //大科室编号
            String bigcode = entry.getKey();
            //大科室编号对应的全部数据
            List<Department> department1List = entry.getValue();

            //封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(department1List.get(0).getBigname());
            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for(Department department : department1List){
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                //封装到List集合
                children.add(departmentVo2);
            }
            //把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);

            //放到最终result里面
            result.add(departmentVo1);
        }
        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null){
            return department.getDepname();
        }
        return null;
    }

}
