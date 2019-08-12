package com.ziyu.admin.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan(basePackages = "com.ziyu.admin.modules.*.mapper")
public class MyBatisConfig {

}
