package com.cc.mall.common.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * cc_mall
 * SpringSecurity定义的用于对密码进行编码以及比对的接口，目前使用的BcryptPasswordEncoder
 *
 * @author Chan
 * @since 2021/1/7 14:29
 **/
@Configuration
public class JwtPasswordEncoder {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
