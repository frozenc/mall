package com.cc.mall.admin.facade;

import com.cc.mall.common.utils.dto.UserLoginDto;
import com.cc.mall.common.utils.dto.UserRegisterDto;
import com.cc.mall.common.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/12 21:20
 **/
@Service
@Transactional
public class UserFacade {

    @Autowired
    private JwtUserService jwtUserService;

    public String login(UserLoginDto userLoginDto) {
        return jwtUserService.login(userLoginDto);
    }

//    public User register(UserRegisterDto userRegisterDto) {
//        return jwtUserService.register(userRegisterDto);
//    }
}
