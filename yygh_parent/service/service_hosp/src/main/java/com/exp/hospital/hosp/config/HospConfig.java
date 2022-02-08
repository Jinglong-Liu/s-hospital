package com.exp.hospital.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.exp.hospital.hosp.mapper")
public class HospConfig {
}
