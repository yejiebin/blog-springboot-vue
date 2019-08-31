package com.yjb.mapper;

import com.yjb.model.pojo.Role;

import java.util.List;

public interface RoleMapper {
    /**
     * 根据用户id查询角色
     * @param userId
     * @return
     */
    List<Role> findUserRoles(Long userId);

    /**
     * 根据角色名称查询Role
     * @param name
     * @return
     */
    Role findRoleByName(String name);

    /**
     * 保存用户角色关系
     * @param userId
     * @param roleId
     */
    void saveUserRoles(Long userId, Long roleId);
}
