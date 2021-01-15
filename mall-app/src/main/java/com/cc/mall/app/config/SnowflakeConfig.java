package com.cc.mall.app.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mall-app
 * 雪花算法配置
 *
 * @author Chan
 * @since 2021/1/13 15:05
 **/
@Configuration
public class SnowflakeConfig {
    @Bean
    public Snowflake getSnowflake() {
        return new Snowflake(0, 0);
    }
}
