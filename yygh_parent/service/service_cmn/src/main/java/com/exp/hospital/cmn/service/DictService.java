package com.exp.hospital.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exp.hospital.model.cmn.Dict;
import com.exp.hospital.model.hosp.HospitalSet;

import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);
}