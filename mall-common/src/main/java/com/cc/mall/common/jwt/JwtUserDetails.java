package com.cc.mall.common.jwt;

import com.cc.mall.mbg.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * tiny
 * 2020/12/27 11:54
 * SpringSecurity需要的用户详情
 * @author Frozen Chan
 * @since
 **/
@Getter
public class JwtUserDetails implements UserDetails {
    private User user;
    private List<String> roleList;
    private List<String> permissionList;
    public JwtUserDetails(User user, List<String> roleList, List<String> permissionList) {
        this.user = user;
        this.roleList = roleList;
        this.permissionList = permissionList;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> collections = new ArrayList<>();
        if (roleList != null) {
            collections.addAll(roleList);
        }
        if (permissionList != null) {
            collections.addAll(permissionList);
        }
        //返回当前用户的权限
        return collections.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
