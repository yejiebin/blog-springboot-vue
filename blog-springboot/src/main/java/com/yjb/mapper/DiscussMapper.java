package com.yjb.mapper;


import com.yjb.model.pojo.Blog;
import com.yjb.model.pojo.Discuss;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussMapper {

    /**
     * 获取最新count 条评论
     *
     * @param count
     * @return
     */
    public List<Discuss> findNewDiscuss(Integer count);

    /**
     * 查询
     *
     * @param userId    用户id
     * @param count 显示数量
     * @return
     */
    List<Discuss> findUserNewDiscuss(Long userId, Integer count);

    /**
     * 获取博文下评论数量
     *
     * @param blogId
     * @return
     */
    Long getDiscussCountByBlogId(Long blogId);

    /**
     * 查询博文下的评论
     *
     * @param blogId
     * @return
     */
    List<Discuss> findDiscussByBlogId(@Param("blogId") Long blogId, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 保存评论
     *
     * @param discuss
     */
    void saveDiscuss(Discuss discuss);

    /**
     * 根据discussId查询评论
     * @param discussId
     * @return
     */
    Discuss findDiscussById(Long discussId);

    /**
     * 根据discussId删除评论
     * @param discussId
     */
    void deleteDiscussById(Long discussId);

    /**
     * 根据Blog查询所有评论
     * @param blog
     * @return
     */
    List<Discuss> findDiscussByBlog(Blog blog);
}
