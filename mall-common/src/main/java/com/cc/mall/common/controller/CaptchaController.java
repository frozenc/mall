package com.cc.mall.common.controller;

import com.cc.mall.common.component.redis.RedisService;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.utils.Constants;
import com.wf.captcha.GifCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/18 16:24
 **/

@Api(tags = "验证码")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private RedisService redisService;

    @ApiOperation("获取验证码")
    @GetMapping
    public CommonResult<Map<String, String>> captcha() {
        // 使用静态验证码
        // SpecCaptcha specCaptcha = new SpecCaptcha(Constants.CODE_WIDTH, Constants.CODE_HEIGHT, Constants.CODE_LENGTH);
        // 动态验证码
        GifCaptcha gifCaptcha = new GifCaptcha(Constants.CODE_WIDTH, Constants.CODE_HEIGHT, Constants.CODE_LENGTH);
        String key = UUID.randomUUID().toString();
        String code = gifCaptcha.text().toLowerCase();
        // 设置60s内输入验证码
        redisService.set(key, code, Constants.CODE_EXPIRE);
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("image", gifCaptcha.toBase64());
        return CommonResult.success(result);
    }
}
