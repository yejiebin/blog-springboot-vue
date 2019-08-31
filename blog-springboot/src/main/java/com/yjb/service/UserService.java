package com.yjb.service;

import com.yjb.config.JwtConfig;
import com.yjb.mapper.MailkeyMapper;
import com.yjb.mapper.RoleMapper;
import com.yjb.mapper.UserMapper;
import com.yjb.model.pojo.Login;
import com.yjb.model.pojo.Mailkey;
import com.yjb.model.pojo.Role;
import com.yjb.model.pojo.User;
import com.yjb.utils.DateUtil;
import com.yjb.utils.JwtTokenUtil;
import com.yjb.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MailkeyMapper mailKeyMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;


    /**
     * 登录
     * 返回token，用户名，用户角色
     *
     * @param user
     * @return
     */
    public Map login(User user) {
        Map<String, Object> map = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = this.loadUserByUsername(user.getName());
            final String token = jwtTokenUtil.generateToken(userDetails);
//            Map<String, Object> claims = new HashMap<>();
//            claims.put('sub', userDetails.getUsername()); //放入用户名
//            claims.put('created', new Date());//放入token生成时间
//            Jwts.builder()
//                    .setClaims(claims)
//                    .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getTime() * 1000))
//                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
//                    .compact();

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
            map.put("token", jwtConfig.getPrefix() + token);
            map.put("name", user.getName());
            map.put("roles", roles);
            return map;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            //认证失败，不返回token
            return null;
        }

    }


    /**
     * 查询用户数
     *
     * @return
     */
    public Long getUserCount() {
        return userMapper.getUserCount("");
    }

    public List<User> findUser(Integer page, Integer showCount) {
        return userMapper.findUser("", (page - 1) * showCount, showCount);
    }

    /**
     * 封禁或解禁用户
     *
     * @param id
     * @param state
     */
    public void updateUserState(Long id, Integer state) {
        User user = new User();
        user.setId(id);
        user.setState(state);
        userMapper.updateUserState(user); //更改用户状态
    }

    /**
     * 根据userName查询用户数
     * @return
     */
    public Long getUserCountByName(String userName) {
        return userMapper.getUserCount(userName);
    }

    /**
     * 根据userName模糊查询用户
     * @param userName
     * @param page
     * @param showCount
     * @return
     */
    public List<User> searchUserByName(String userName, Integer page, Integer showCount) {
        return userMapper.findUser(userName, (page - 1) * showCount, showCount);
    }

    /**
     * 用户注册
     * @param user
     * @param mailCode
     */
    @Transactional
    public void register(User user, String mailCode) {
        //验证邮箱验证码
        Mailkey mail = mailKeyMapper.findMailkeyByCodeAndMail(mailCode, user.getMail());
        if (mail == null || DateUtil.dateDiffMinute(mail.getSendTime()) > 5) {
            throw new RuntimeException("验证码无效");
        }

        //有效 保存用户
        if (userMapper.findUserByName(user.getName()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        if (userMapper.findUserByMail(user.getMail()) != null) {
            throw new RuntimeException("邮箱已被绑定");
        }

        List<Role> roles = new ArrayList<>(1);
        roles.add(roleMapper.findRoleByName("USER"));   //新用户赋予USER角色
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword())); //加密密码
        user.setState(1);   //设置状态
        userMapper.saveUser(user);  //保存用户

        for (Role role : roles) {   //保存用户角色关系
            roleMapper.saveUserRoles(user.getId(), role.getId());
        }

        mailKeyMapper.deleteMailkeyByMail(user.getMail());  //删除验证码
    }

    /**
     * 根据用户名查询用户及用户角色
     *
     * @param name
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userMapper.findUserByName(name);
        if (user == null) {  //查询不到用户时，判定这是个非法token，但是仍然返回 不抛异常
            return new org.springframework.security.core.userdetails.User("NORMAL", "NORMAL", null);
            //也可将异常抛出
//            throw new UsernameNotFoundException("USER NOT FOUND");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //用于添加用户的权限。将用户权限添加到authorities
        if ((user.getState() == 1)) {
            List<Role> roles = roleMapper.findUserRoles(user.getId());
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        } else {  //该用户被封禁
            authorities.add(new SimpleGrantedAuthority("NORMAL"));
        }
        //查询到了用户，即用户进行了操作或登录动作，记录在登录表
        Login login = loginService.findLoginByUser(user);
        if (login == null) {
            loginService.saveLoginInfo(user);
        }else {
            login.setIp(RequestUtil.getIpAddress(request));
            login.setTime(DateUtil.getCurrentDate());
            loginService.updateLoginInfo(login);
        }
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);

    }

    /**
     * 修改密码
     * @param newPassword
     * @param code
     */
    @Transactional
    public void updateUserPassword(String newPassword, String code) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);

        //校验邮箱验证码
        Mailkey mailkey = mailKeyMapper.findMailkeyByCodeAndMail(code, user.getMail());
        if (mailkey == null || DateUtil.dateDiffMinute(mailkey.getSendTime()) > 5) {    //判断验证码是否有效
            throw new RuntimeException("验证码无效");
        }
        //更新密码
        user.setPassword(encoder.encode(newPassword));
        userMapper.updateUserPassword(user);
        //删除验证码
        mailKeyMapper.deleteMailkeyByMail(user.getMail());
    }

    /**
     * 用户忘记密码
     * @param username
     * @param newPassword
     * @param mailCode
     */
    @Transactional
    public void forgetPassword(String username, String newPassword, String mailCode) {
        User user = userMapper.findUserByName(username);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }

        Mailkey mailkey = mailKeyMapper.findMailkeyByCodeAndMail(mailCode, user.getMail());
        if (mailkey == null || DateUtil.dateDiffMinute(mailkey.getSendTime()) > 5) {
            throw new RuntimeException("验证码无效");
        }
        //修改密码
        user.setPassword(encoder.encode(newPassword));
        userMapper.updateUserPassword(user);
        //删除验证码
        mailKeyMapper.deleteMailkeyByMail(mailkey.getMail());
    }

    /**
     * 改绑邮箱
     * @param newMail
     * @param oldMailCode
     * @param newMailCode
     */
    @Transactional
    public void updateMail(String newMail, String oldMailCode, String newMailCode) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);
        //验证旧邮箱验证码
        Mailkey oldMailkey = mailKeyMapper.findMailkeyByCodeAndMail(oldMailCode, user.getMail());
        if (oldMailkey == null || DateUtil.dateDiffMinute(oldMailkey.getSendTime()) > 5) {
            throw new RuntimeException("旧邮箱无效验证码");
        }

        //检查新邮箱是否已存在
        if (userMapper.findUserByMail(newMail) != null)
            throw new RuntimeException("此邮箱已使用");

        //验证新邮箱验证码
        Mailkey newMailkey = mailKeyMapper.findMailkeyByCodeAndMail(newMailCode, newMail);
        if (newMailkey == null || DateUtil.dateDiffMinute(newMailkey.getSendTime()) > 5) {
            throw new RuntimeException("验证码无效");
        }
        //更改新邮箱
        user.setMail(newMailkey.getMail());
        userMapper.updateUserMail(user);
        //删除验证码
        mailKeyMapper.deleteMailkeyByMail(oldMailkey.getMail());
        mailKeyMapper.deleteMailkeyByMail(newMailkey.getMail());

    }

    public String getMail() {
        return userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request)).getMail();
    }
}
