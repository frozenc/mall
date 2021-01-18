package com.cc.mall.common.config;


import com.cc.mall.common.jwt.JwtAuthenticationTokenFilter;
import com.cc.mall.common.jwt.JwtRestAuthenticationEntryPoint;
import com.cc.mall.common.jwt.JwtRestfulAccessDeniedHandler;
import com.cc.mall.common.jwt.JwtUserDetails;
import com.cc.mall.mbg.entity.User;
import com.cc.mall.common.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * tiny
 * 2020/12/25 21:13
 * SpringSecurity的配置
 *
 * @author Frozen Chan
 * @since
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtUserService userService;
    @Autowired
    private JwtRestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private JwtRestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable() // 使用jwt不需要csrf
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //基于Token，不需要session
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-ui/",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                ).permitAll()
                .antMatchers("/return", "/notify").permitAll() // 支付回调
                .antMatchers("/login", "/register", "/captcha").permitAll() //登录注册要允许匿名访问
                .antMatchers(HttpMethod.GET, "/product", "/category", "/test").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll() //跨域请求会先进性一次options请求
//                .antMatchers("/**").permitAll() // 测试时全部运行访问
                .anyRequest().authenticated(); //除上面外的所有请求全部需要鉴权认证
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                List<String> permissionList = userService.getPermissionList(user.getId());
                List<String> roleList = userService.getRoleList(user.getId());
                return new JwtUserDetails(user, roleList, permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
