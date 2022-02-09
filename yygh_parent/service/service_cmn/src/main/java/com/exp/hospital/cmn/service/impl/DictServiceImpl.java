package com.exp.hospital.cmn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exp.hospital.cmn.mapper.DictMapper;
import com.exp.hospital.cmn.service.DictService;
import com.exp.hospital.model.cmn.Dict;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

}
