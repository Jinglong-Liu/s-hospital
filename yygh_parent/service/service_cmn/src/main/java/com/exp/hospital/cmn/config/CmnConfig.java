package com.exp.hospital.cmn.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.exp.hospital.cmn.mapper")
public class CmnConfig {
}
