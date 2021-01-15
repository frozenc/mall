package com.cc.mall.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cc.mall.common.utils.dto.UserLoginDto;
import com.cc.mall.common.utils.dto.UserRegisterDto;
import com.cc.mall.common.component.redis.service.RedisService;
import com.cc.mall.common.dao.UserRolePermissionDao;
import com.cc.mall.common.jwt.JwtTokenUtil;
import com.cc.mall.common.jwt.JwtUserDetailsService;
import com.cc.mall.mbg.entity.Permission;
import com.cc.mall.mbg.entity.Role;
import com.cc.mall.mbg.entity.User;
import com.cc.mall.mbg.mapper.UserMapper;
import com.cc.mall.common.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/11 13:21
 **/
@Service
public class JwtUserServiceImpl extends ServiceImpl<UserMapper, User> implements JwtUserService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRolePermissionDao userRolePermissionDao;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<String> getRoleList(Long id) {
        // user_id -> role_id
        //用户表查角色表
        return userRolePermissionDao.getRoleList(id).stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermissionList(Long id) {
        //user_id -> role_id -> permission_id
        //用户表查角色表查权限表
//        return new ArrayList<>();
        return userRolePermissionDao.getPermissionList(id).stream()
                .map(Permission::getName)
                .collect(Collectors.toList());
    }

//    @Override
//    public User register(UserRegisterDto userRegisterDto) {
//        User user = new User();
//        BeanUtils.copyProperties(userRegisterDto, user);
//        //查询是否有重复用户
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
//                .eq("username", user.getUsername());
//        List<User> userList = userMapper.selectList(queryWrapper);
//        if (userList.size() > 0) {
//            return null;
//        }
//        //对密码进行加密
//        String encodePassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodePassword);
//        userMapper.insert(user);
//        return user;
//    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDto.getUsername());
            if (!passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (Exception e) {
            log.warn("登录异常：" + e.getMessage());
        }
        return token;
    }
}
