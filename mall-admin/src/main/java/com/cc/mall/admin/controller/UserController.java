package com.cc.mall.admin.controller;

import cn.hutool.core.convert.Convert;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.dto.UserLoginDto;
import com.cc.mall.common.utils.dto.UserRegisterDto;
import com.cc.mall.common.utils.vo.UserInfoVo;
import com.cc.mall.admin.facade.UserFacade;
import com.cc.mall.common.jwt.JwtUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/11 16:09
 **/
@Api(tags = "用户")
@RestController
@RequestMapping("")
@PropertySource("classpath:jwt.properties")
public class UserController {
    @Autowired
    private UserFacade userFacade;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("获取用户信息")
    @GetMapping("/user")
    public CommonResult<UserInfoVo> getUserInfo(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        UserInfoVo userInfoVo = Convert.convert(UserInfoVo.class, userDetails.getUser());
        return CommonResult.success(userInfoVo);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    @ResponseBody
    public CommonResult login(@Valid UserLoginDto userLoginDto) {
        String token = userFacade.login(userLoginDto);
        if (token == null) {
            return CommonResult.validateFailed("用户名或者密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);

    }

//    @ApiOperation("注册")
//    @PostMapping("/register")
//    @ResponseBody
//    public CommonResult<User> register(@Valid UserRegisterDto userRegisterDto) {
//        User user = userFacade.register(userRegisterDto);
//        if (user == null) {
//            return CommonResult.failed("用户名已注册");
//        }
//        return CommonResult.success(user);
//    }
}
