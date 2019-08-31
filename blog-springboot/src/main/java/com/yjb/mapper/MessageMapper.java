package com.yjb.mapper;

import com.yjb.model.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {

    void saveMessage(Message message);

    Long getMessageCount();

    List<Message> findMessage(@Param("start") Integer start, @Param("showCount") Integer showCount);

    void deleteMessageById(Long messageId);
}
