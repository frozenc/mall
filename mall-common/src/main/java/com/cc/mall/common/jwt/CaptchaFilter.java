package com.cc.mall.common.jwt;

import com.cc.mall.common.component.redis.RedisService;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.common.utils.utils.RenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * mall-app
 * 验证码filter
 *
 * @author Chan
 * @since 2021/1/18 16:40
 **/
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getMethod().equals("POST") &&
                (httpServletRequest.getRequestURI().equals("/login") || httpServletRequest.getRequestURI().equals("/register"))) {
            // 获取参数中的验证码
            String key = httpServletRequest.getParameter("key") == null ? "" : httpServletRequest.getParameter("key");
            String code = httpServletRequest.getParameter("code") == null ? "" : httpServletRequest.getParameter("code");
            // 获取缓存中的验证码
            String redisCode = (String) redisService.get(key);
            // 判断验证码是否一致
            if (redisCode == null) {
                RenderUtil.render(httpServletResponse, CommonResult.failed(ResultCode.CAPTCHA_EXPIRE));
            } else if (!code.equals(redisCode)) {
                RenderUtil.render(httpServletResponse, CommonResult.failed(ResultCode.CAPTCHA_ERROR));
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
