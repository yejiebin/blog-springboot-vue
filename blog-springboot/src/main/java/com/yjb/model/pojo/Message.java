package com.yjb.model.pojo;

import lombok.Data;

/**
 * 留言
 */
@Data
public class Message {
    private Long id;//id
    private String name;//游客显示为ip地址
    private String body;//留言内容
}
