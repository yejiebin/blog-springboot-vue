package com.yjb.service;

import com.yjb.mapper.LoginMapper;
import com.yjb.model.pojo.Login;
import com.yjb.model.pojo.User;
import com.yjb.utils.DateUtil;
import com.yjb.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoginService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginMapper loginMapper;

    /**
     * 保存登录信息
     *
     * @param user
     */
    @Transactional
    public void saveLoginInfo(User user) {
        Login login = new Login();
        login.setUser(user);//绑定用户
        login.setIp(RequestUtil.getIpAddress(request));//获取操作ip
        login.setTime(DateUtil.getCurrentDate());//操作时间
        loginMapper.saveLogin(login);
    }

    public Login findLoginByUser(User user) {
        return loginMapper.findLoginByUser(user);
    }

    public void updateLoginInfo(Login login) {
        loginMapper.updateLogin(login);
    }
}
