package com.yjb.mapper;

import com.yjb.model.pojo.Blog;
import com.yjb.model.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogMapper {

    /**
     * 保存博客
     *
     * @param blog
     */
    void saveBlog(Blog blog);

    /**
     * 查询热门博文
     *
     * @param count 显示数量
     * @return
     */
    List<Blog> findHotBlog(Integer count);

    /**
     * 按月份归档博客
     *
     * @param count 最近几个月
     * @return month 月
     * year 年
     * count 数量
     */
    List<Map> statisticalBlogByMonth(Integer count);

    /**
     * 查询主页博客数量
     *
     * @return
     */
    Long getHomeBlogCount();

    /**
     * 查询主页博客
     *
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> findHomeBlog(@Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 根据id查询博文
     *
     * @param blogId
     * @return
     */
    Blog findBlogById(Long blogId);

    /**
     * 更新博文浏览数
     *
     * @param blog
     */
    void updateBlogViews(Blog blog);

    /**
     * 修改博文
     *
     * @param blog
     */
    void updateBlog(Blog blog);

    /**
     * 保存博文标签
     *
     * @param blogId
     * @param tagId
     */
    void saveBlogTag(@Param("blogId") Long blogId, @Param("tagId") Long tagId);

    /**
     * 更改博客状态
     *
     * @param blogId
     * @param blogState
     */
    void updateBlogState(@Param("blogId") Long blogId, @Param("blogState") Integer blogState);

    /**
     * 根据tagId查询博客数
     *
     * @param tagId
     * @return
     */
    Long findBlogCountByTagId(Long tagId);

    /**
     * 博客评论数+1
     *
     * @param blog
     */
    void updateBlogDiscussCount(Blog blog);

    /**
     * 搜索博文标题，内容
     *
     * @param searchText
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> searchBlog(@Param("searchText") String searchText, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 符合关键词的博文数量
     *
     * @param searchText
     * @return
     */
    Long getSearchBlogCount(String searchText);

    /**
     * 返回博客的所有数量（包括删除状态）
     *
     * @return
     */
    Long getAllBlogCount();

    /**
     * 分页查询所有博客
     *
     * @param start
     * @param showCount
     * @return
     */
    List<Blog> findAllBlog(@Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 管理员分页模糊查询
     *
     * @param searchText
     * @return
     */
    Long getSearchAllBlogCount(String searchText);

    /**
     * 管理员分页模糊查询所有博客
     * @param searchText
     * @param start
     * @param showCount
     * @return
     */
    List searchAllBlog(@Param("searchText") String searchText, @Param("start") Integer start, @Param("showCount") Integer showCount);

    /**
     * 根据用户查询用户博客数量（正常状态）
     * @param user
     * @return
     */
    Long getBlogCountByUser(User user);

    List<Blog> findBlogByUser(@Param("user") User user, @Param("start") Integer start, @Param("showCount") Integer showCount);
}
