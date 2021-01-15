package com.cc.mall.mbg.service.impl;

import com.cc.mall.mbg.entity.User;
import com.cc.mall.mbg.mapper.UserMapper;
import com.cc.mall.mbg.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2021-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
