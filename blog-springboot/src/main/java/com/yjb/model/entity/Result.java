package com.yjb.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应实体
 */
@Data
@ApiModel("响应实体")
public class Result {
    @ApiModelProperty(value = "返回状态码", dataType = "Integer")
    private Integer code;
    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String message;
    @ApiModelProperty(value = "返回数据", dataType = "Object")
    private Object data;

    private Result() {}

    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result create(Integer code, String message) {
        return new Result(code, message);
    }

    public static Result create(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }
}
