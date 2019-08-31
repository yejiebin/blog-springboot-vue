package com.yjb.mapper;

import com.yjb.model.pojo.Code;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CodeMapper {

    /**
     * 保存激活码
     *
     * @param code
     */
    void saveCode(Code code);

    /**
     * 获取记录条数
     *
     * @return
     */
    Long getCodeCount();

    /**分页查询激活码
     * @param start
     * @param showCount
     * @return
     */
    List<Code> findCode(@Param("start") Integer start, @Param("showCount") Integer showCount);
}
