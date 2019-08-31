package com.yjb.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户
 */
@Data
@ApiModel("用户")
public class User {
    @ApiModelProperty(value = "用户Id", dataType = "Long")
    private Long id;
    @ApiModelProperty(value = "用户名", dataType = "String")
    private String name;
    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;
    @ApiModelProperty(value = "邮箱", dataType = "String")
    private String mail;
    @ApiModelProperty(value = "状态", dataType = "Integer")
    private Integer state;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Role> roles;//角色

    private Login login;//登录信息
}
