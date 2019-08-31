package com.yjb.controller;

import com.yjb.model.entity.PageResult;
import com.yjb.model.entity.Result;
import com.yjb.model.entity.StatusCode;
import com.yjb.model.pojo.Mailkey;
import com.yjb.model.pojo.User;
import com.yjb.service.MailkeyService;
import com.yjb.service.UserService;
import com.yjb.utils.DateUtil;
import com.yjb.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@Api(tags = "用户api", description = "用户api", basePath = "/user")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailkeyService mailkeyService;

    /**
     * 登录返回token
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户名+密码 name+password 返回token")
    @PostMapping("/login")
    public Result login(User user) {
        if (!FormatUtil.checkStringNull(user.getName(), user.getPassword()))
            return Result.create(StatusCode.ERROR, "参数错误");

        Map map = userService.login(user);
        if (map != null) {
            //登录成功更新登录表
            return Result.create(StatusCode.OK, "登录成功", map);
        } else {
            return Result.create(StatusCode.LOGINERROR, "登录失败，用户名或密码错误");
        }
    }

    /**
     * 发送验证邮件
     * 同步发送
     *
     * @param mail
     * @return
     */
    @ApiOperation(value = "发送验证邮件", notes = "mail 冷却五分钟")
    @PostMapping("/sendMail")
    public Result sendMail(String mail) {

        //邮箱格式校验
        if (!(FormatUtil.checkStringNull(mail)) || (!FormatUtil.checkMail(mail)))
            return Result.create(StatusCode.ERROR, "邮箱格式错误");

        Mailkey mailkey = mailkeyService.findMailkeyByMail(mail);
        if (mailkey != null) {  //是否发送过邮箱;
            if (DateUtil.dateDiffMinute(mailkey.getSendTime()) > 5) {//上一次发送距现在超过五分钟
                mailkeyService.sendMail(mail);
                return Result.create(StatusCode.OK, "已发送验证码");
            }else {
                return Result.create(StatusCode.ERROR, "5分钟内不可重复发送验证码");
            }
        }else {
            mailkeyService.sendMail(mail);
            return Result.create(StatusCode.OK, "已发送验证码");
        }
    }

    @ApiOperation(value = "用户注册", notes = "用户名+密码+邮箱+邮箱验证码+邀请码 name+password+mail+mailCode+inviteCode")
    @PostMapping("/register")
    public Result register(User user, String mailCode) {
        if (!FormatUtil.checkStringNull(user.getName(), user.getPassword(), user.getMail(), mailCode)) {
            return Result.create(StatusCode.ERROR, "注册失败,参数不完整");
        }
        try {
            userService.register(user, mailCode);
            return Result.create(StatusCode.OK, "注册成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "注册失败，" + e.getMessage());
        }
    }

    @ApiOperation(value = "用户修改密码", notes = "旧密码+新密码+验证码")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/updatePassword")
    public Result updatePassword(String newPassword, String code) {
        if (!FormatUtil.checkStringNull(newPassword, code))
            return Result.create(StatusCode.ERROR, "参数错误");
        try {
            userService.updateUserPassword(newPassword, code);
            return Result.create(StatusCode.OK, "修改密码成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "改绑邮箱")
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/updateMail")
    public Result updateMail(String newMail, String oldMailCode, String newMailCode) {
        if (!FormatUtil.checkStringNull(newMail, oldMailCode, newMailCode)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        //检查邮箱格式
        if (!FormatUtil.checkMail(newMail))
            return Result.create(StatusCode.ERROR, "参数错误");
        try {
            userService.updateMail(newMail, oldMailCode, newMailCode);
            return Result.create(StatusCode.OK, "帐户新邮箱为："+newMail);
        } catch (Exception e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }

    }

    @ApiOperation(value = "用户忘记密码", notes = "用户名+新密码+验证码")
    @PostMapping("/forgetPassword")
    public Result forgetPassword(String username, String newPassword, String mailCode) {
        if (!FormatUtil.checkStringNull(newPassword, newPassword, mailCode))
            return Result.create(StatusCode.ERROR, "参数错误");
        try {
            userService.forgetPassword(username, newPassword, mailCode);
            return Result.create(StatusCode.OK, "修改密码成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "分页查询用户", notes = "页码+显示数量")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{page}/{showCount}")
    public Result getUser(@PathVariable("page") Integer page, @PathVariable("showCount") Integer showCount) {
        if (!FormatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.ERROR, "参数错误");
        PageResult<User> pageResult =
                new PageResult(userService.getUserCount(), userService.findUser(page, showCount));
        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 用户封禁或解禁
     *
     * @param id
     * @param state
     * @return
     */
    @ApiOperation(value = "用户封禁或解禁", notes = "用户id+状态 id+state")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ban/{id}/{state}")
    public Result banUser(@PathVariable Long id, @PathVariable Integer state) {

        if (!FormatUtil.checkObjectNull(id, state))
            return Result.create(StatusCode.ERROR, "参数错误");


        if (state == 0) {
            userService.updateUserState(id, state);
            return Result.create(StatusCode.OK, "封禁成功");
        } else if (state == 1) {
            userService.updateUserState(id, state);
            return Result.create(StatusCode.OK, "解禁成功");
        } else
            return Result.create(StatusCode.ERROR, "参数错误");
    }

    @ApiOperation(value = "根据用户名分页搜索用户", notes = "页码+显示数量+搜索内容")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search/{page}/{showCount}")
    public Result searchUser(String userName, @PathVariable Integer page, @PathVariable Integer showCount) {
        if (!FormatUtil.checkStringNull(userName))
            return Result.create(StatusCode.ERROR, "参数错误");
        if (!FormatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.ERROR, "参数错误");
        PageResult<User> pageResult =
                new PageResult(userService.getUserCountByName(userName), userService.searchUserByName(userName, page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "获取邮箱", notes = "获取邮箱")
    @GetMapping("/mail")
    public Result getMail() {
        return Result.create(StatusCode.OK, "查询成功", userService.getMail());
    }
}
