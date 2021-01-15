package com.cc.mall.common.jwt;

import cn.hutool.json.JSONUtil;
import com.cc.mall.common.utils.utils.Constants;
import com.cc.mall.common.component.redis.service.RedisService;
import com.cc.mall.mbg.entity.User;
import com.cc.mall.common.service.JwtUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/11 12:39
 **/

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private JwtUserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从redis取用户
        User user = (User) redisService.get(Constants.USER_KEY + username); // get
        if (user == null) {
            // 从mysql取用户
            user = userService.getUserByUsername(username);
            if (user != null) {
                log.info("缓存用户：" + JSONUtil.toJsonStr(user));
                redisService.set(Constants.USER_KEY + username, user, Constants.USER_EXPIRE); // set
            } else {
                throw new UsernameNotFoundException("用户名不存在");
            }
        }
        // 从redis取角色
        List<String> roleList = (List<String>) redisService.get(Constants.ROLE_KEY + username); // get
        if (roleList == null) {
            // 从mysql取角色
            roleList = userService.getRoleList(user.getId());
            log.info("缓存角色：" + roleList.toString());
            redisService.set(Constants.ROLE_KEY + username, roleList, Constants.ROLE_EXPIRE); // set
        }
        // 从redis取权限
        List<String> permissionList = (List<String>) redisService.get(Constants.PERMISSION_KEY + username); // get
        if (permissionList == null) {
            // 从mysql取权限
            permissionList = userService.getPermissionList(user.getId());
            log.info("缓存权限：" + permissionList.toString());
            redisService.set(Constants.PERMISSION_KEY + username, permissionList, Constants.PERMISSION_EXPIRE); // set
        }
        JwtUserDetails userDetails = new JwtUserDetails(user, roleList, permissionList);
        log.info("认证成功：" + JSONUtil.toJsonStr(userDetails));
        return userDetails;
    }
}
