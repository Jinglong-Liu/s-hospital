package com.exp.hospital.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exp.hospital.hosp.mapper.HospitalSetMapper;
import com.exp.hospital.hosp.service.HospitalSetService;
import com.exp.hospital.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

}
