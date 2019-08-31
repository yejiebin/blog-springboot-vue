package com.yjb.model.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 博文
 */
@Data
public class Blog {
    private Long id;//id
    private String title;//标题
    private String body;//内容
    private Integer discussCount;//评论数
    private Integer blogViews;//浏览数
    private Date time;//发布时间
    private Integer state;//博文状态--0删除 1正常

    private User user;//用户

    private List<Tag> tags;//博文对应的标签
}
