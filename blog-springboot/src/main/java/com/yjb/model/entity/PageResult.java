package com.yjb.model.entity;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 * @param <T>
 */
@Data
public class PageResult<T> {
    private Long total;     //数据条数
    private List<T> rows;   //数据

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
