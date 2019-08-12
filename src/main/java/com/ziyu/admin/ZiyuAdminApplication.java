package com.ziyu.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * \* User: ziyu
 * \* Date: 2019/7/3
 * \* Description: 启动类
 * \
 */
@SpringBootApplication(exclude = {QuartzAutoConfiguration.class})
@EnableTransactionManagement
@EnableWebMvc
@EnableCaching
@EnableSwagger2
public class ZiyuAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZiyuAdminApplication.class, args);
    }
}
