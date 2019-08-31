package com.yjb.model.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 登录表
 */
@Data
public class Login {
    private Long id;  //主键
    private User user;//用户
    private String ip;//最后登录ip
    private Date time;//最后登录时间
}
