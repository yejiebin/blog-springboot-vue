package com.yjb.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Tag {
    private Long id;//id
    private String name;//标签名

    @JsonIgnore
    private User user;//用户
}
