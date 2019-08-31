package com.yjb.mapper;

import com.yjb.model.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {

    /**
     * 查询博文的所有标签
     * @param blogId
     * @return
     */
    List<Tag> findTagByBlogId(Long blogId);

    /**
     * 根据博文id删除标签
     * @param blogId
     */
    void deleteTagByBlogId(Long blogId);

    /**
     * 新增标签
     * @param tag
     */
    void saveTag(Tag tag);

    /**
     * 根据标签名查询标签(忽略大小写)
     * @return
     */
    Tag findTagByTagName(String tagName);

    /**
     * 根据id查询标签
     * @param tagId
     * @return
     */
    Tag findTagById(Long tagId);

    /**
     *根据tag id删除标签
     * @param tagId
     */
    void deleteTag(Long tagId);

    /**
     * 根据tag id更改标签
     * @param tag
     */
    void updateTagName(Tag tag);

    /**
     * 查询该user id下的所有标签
     * @param userId
     */
    List<Tag> findTagByUserId(Long userId);

    /**
     * 根据userId与tagName查询Tag
     * @param tagName
     * @param userId
     * @return
     */
    Tag findTagByTagNameAndUserId(@Param("tagName") String tagName, @Param("userId") Long userId);
}
