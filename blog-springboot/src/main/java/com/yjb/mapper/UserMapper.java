package com.yjb.mapper;

import com.yjb.model.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    User findUserByName(String username);

    /**
     * 查询用户数
     *
     * @return
     */
    Long getUserCount(String keyword);

    /**
     * 分页查询用户
     *
     * @param keyword
     * @param start
     * @param showCount
     * @return
     */
    List<User> findUser(@Param("keyword") String keyword, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 更改用户状态
     * @param user
     */
    void updateUserState(User user);

    /**
     * 根据邮箱查询用户
     * @param mail
     * @return
     */
    User findUserByMail(String mail);

    /**
     * 新增用户
     * @param user
     */
    void saveUser(User user);

    /**
     * 修改密码（可根据user唯一用户属性）
     * @param user
     */
    void updateUserPassword(User user);

    /**
     * 更改邮箱（可根据user唯一用户属性）
     * @param user
     */
    void updateUserMail(User user);
}
