package com.yjb.service;

import com.yjb.mapper.MailkeyMapper;
import com.yjb.model.entity.MailMessage;
import com.yjb.model.pojo.Mailkey;
import com.yjb.utils.DateUtil;
import com.yjb.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailkeyService {

    @Autowired
    private MailkeyMapper mailkeyMapper;

    @Autowired
    private MailMessage mailMessage;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 根据邮箱查询验证码
     * @param mail
     * @return
     */
    public Mailkey findMailkeyByMail(String mail) {
        return mailkeyMapper.findMailkeyByMail(mail);
    }

    /**
     * 发送邮箱验证码
     * @param mail
     */
    @Transactional
    public void sendMail(String mail) {
        //发送邮件
        Integer random = RandomUtil.nextInt(100000, 999999);//貌似线程不安全 范围100000 - 999999
        mailSender.send(mailMessage.create(mail, "主题：邮箱验证码", "博客--邮箱验证码：" + random + "，五分钟内有效"));
        //保存发送记录
        mailkeyMapper.deleteMailkeyByMail(mail);//删除原有记录 无论有没有
        Mailkey mailkey = new Mailkey();
        mailkey.setMail(mail);
        mailkey.setCode(random.toString());
        mailkey.setSendTime(DateUtil.getCurrentDate());
        mailkeyMapper.saveMailkey(mailkey);
    }
}
