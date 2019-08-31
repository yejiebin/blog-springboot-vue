package com.yjb.mapper;

import com.yjb.model.pojo.Login;
import com.yjb.model.pojo.User;

public interface LoginMapper {
    void updateLogin(Login login);

    Login findLoginByUser(User user);

    void saveLogin(Login login);
}
