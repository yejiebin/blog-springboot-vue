package com.yjb.model.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 邮箱验证码
 */
@Data
public class Mailkey {
    private Long id;
    private String mail;//发送邮箱
    private String code;//验证码
    private Date sendTime;//发送时间
}
