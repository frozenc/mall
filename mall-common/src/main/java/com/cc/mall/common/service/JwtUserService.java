package com.cc.mall.common.service;

import com.cc.mall.common.utils.dto.UserLoginDto;
import com.cc.mall.common.utils.dto.UserRegisterDto;
import com.cc.mall.mbg.entity.User;

import java.util.List;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/11 12:41
 **/
public interface JwtUserService {
    /**
     * 根据用户名获取用户对象
     */
    User getUserByUsername(String username);

    /**
     * 登录功能
     */
    String login(UserLoginDto userLoginDto);

    /**
     * 注册功能
     */
//    User register(UserRegisterDto registerDto);

    /**
     * 获取用户角色
     */
    List<String> getRoleList(Long id);

    /**
     * 获取用户所有权限
     */
    List<String> getPermissionList(Long id);
}
