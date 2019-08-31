package com.yjb.model.pojo;

import lombok.Data;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.util.Date;

/**
 * 回复
 */
@Data
public class Reply {
    private Long id;        //id
    private String body;    //回复内容
    private Date time;      //回复时间
    private User user;      //用户
    private Discuss discuss;//评论
    private Reply reply;    //父节点回复
}
