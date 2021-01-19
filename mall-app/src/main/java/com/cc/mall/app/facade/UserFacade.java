package com.cc.mall.app.facade;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cc.mall.common.utils.dto.RegisterDto;
import com.cc.mall.common.utils.dto.UserInfoDto;
import com.cc.mall.common.utils.dto.UserLoginDto;
import com.cc.mall.common.utils.dto.UserRegisterDto;
import com.cc.mall.common.utils.enums.UserRoleEnum;
import com.cc.mall.common.utils.utils.Constants;
import com.cc.mall.common.component.redis.RedisService;
import com.cc.mall.common.service.JwtUserService;
import com.cc.mall.mbg.entity.User;
import com.cc.mall.mbg.entity.UserRole;
import com.cc.mall.mbg.service.UserRoleService;
import com.cc.mall.mbg.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 9:44
 **/
@Service
public class UserFacade {

    @Autowired
    private JwtUserService jwtUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(UserLoginDto userLoginDto) {
        return jwtUserService.login(userLoginDto);
    }

    public User register(RegisterDto registerDto) {
        User user = new User();
        BeanUtils.copyProperties(registerDto, user);
        //查询是否有重复用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", user.getUsername());
        List<User> userList = userService.list(queryWrapper);
        if (userList.size() > 0) {
            return null;
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userService.save(user);
        user = userService.getOne(queryWrapper);
        UserRole userRole = new UserRole()
                .setRoleId(UserRoleEnum.USER.getCode().longValue())
                .setUserId(user.getId());
        userRoleService.save(userRole);
        return user;
    }

    public void updateUserInfoByUsername(String username, UserInfoDto userInfoDto) {
        User user = Convert.convert(User.class, userInfoDto);
        userService.update(user, Wrappers.<User>lambdaUpdate().eq(User::getUsername, username));
        redisService.delete(Constants.USER_KEY + username); //刷新缓存
    }

    public void updatePasswordByUsername(String username, String password) {
        User user = new User().setPassword(passwordEncoder.encode(password));
        userService.update(user, Wrappers.<User>lambdaUpdate().eq(User::getUsername, username));
        redisService.delete(Constants.USER_KEY + username);
    }
}
