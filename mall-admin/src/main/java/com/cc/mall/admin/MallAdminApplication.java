package com.cc.mall.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.cc.mall.mbg.mapper", "com.cc.mall.common.dao", "com.cc.mall.admin.dao"})
@ComponentScan(basePackages = {"com.cc.mall.mbg", "com.cc.mall.admin", "com.cc.mall.common"})
public class MallAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallAdminApplication.class, args);
    }

}
