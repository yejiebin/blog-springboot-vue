package com.yjb.mapper;

import com.yjb.model.pojo.Reply;

import java.util.List;

public interface ReplyMapper {

    /**
     * 根据评论id查询回复
     * @param discussId
     * @return
     */
    List<Reply> findReplyByDiscussId(Long discussId);

    /**
     * 根据id查询回复
     * @param replyId
     * @return
     */
    Reply findReplyById(Long replyId);

    /**
     * 保存回复
     * @param reply
     */
    void saveReply(Reply reply);

    /**
     * 根据discussId级联删除reply
     * @param discussId
     * @return
     */
    Long deleteReplyByDiscussId(Long discussId);

    /**
     * 根据id删除回复
     * @param replyId
     */
    void deleteReplyById(Long replyId);
}
