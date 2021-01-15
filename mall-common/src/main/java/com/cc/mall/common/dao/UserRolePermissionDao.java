package com.cc.mall.common.dao;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/11 21:12
 **/

import com.cc.mall.mbg.entity.Permission;
import com.cc.mall.mbg.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 自定义映射
 */
public interface UserRolePermissionDao {
    /**
     * 获取用户所有角色
     */
    @Select("SELECT r.*" +
            " FROM user u" +
            " LEFT JOIN user_role ur ON u.id = ur.user_id" +
            " LEFT JOIN role r ON ur.role_id = r.id" +
            " WHERE u.id=#{userId}")
    List<Role> getRoleList(@Param("userId") Long userId);

    /**
     * 获取用户所有权限
     */
    @Select("SELECT r.*" +
            " FROM user u" +
            " LEFT JOIN user_role ur ON u.id = ur.user_id" +
            " LEFT JOIN role r ON ur.role_id = r.id" +
            " LEFT JOIN role_permission rp ON r.id = rp.role_id" +
            " LEFT JOIN permission p ON p.id = rp.permission_id" +
            " WHERE u.id=#{userId}")
    List<Permission> getPermissionList(@Param("userId") Long userId);
}
