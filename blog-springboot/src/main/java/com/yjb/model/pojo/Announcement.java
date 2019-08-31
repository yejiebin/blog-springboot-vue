package com.yjb.model.pojo;

import lombok.Data;

/**
 * 公告
 */
@Data
public class Announcement {
    private Long id;//id
    private String title;//公告标题
    private String body;//公告内容
    private Integer top;//是否置顶0 置顶 1未置顶
}
