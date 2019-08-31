package com.yjb.service;

import com.yjb.mapper.BlogMapper;
import com.yjb.mapper.TagMapper;
import com.yjb.mapper.UserMapper;
import com.yjb.model.pojo.Tag;
import com.yjb.model.pojo.User;
import com.yjb.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class TagService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    HttpServletRequest request;

    /**
     * 新增标签
     *
     * @param tagName
     */
    public Tag saveTag(String tagName) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        if (tagMapper.findTagByTagNameAndUserId(tagName, user.getId()) != null) {
            throw new RuntimeException("标签重复");
        }

        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setUser(user);
        tagMapper.saveTag(tag);
        return tag;
    }

    /**
     * 删除标签
     *
     * @param tagId
     */
    @Transactional
    public void deleteTagById(Long tagId) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Tag tag = tagMapper.findTagById(tagId);
        if (tag.getUser().getId() != user.getId()) {
            throw new RuntimeException("无权删除此标签");
        }
        //查询此标签下是否有博文
        if (blogMapper.findBlogCountByTagId(tagId) > 0)
            throw new RuntimeException("此标签关联了博客");
        tagMapper.deleteTag(tagId);
    }

    /**
     * 更改标签
     *
     * @param tagId
     * @param tagName
     */
    public void updateTag(Long tagId, String tagName) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Tag tag = tagMapper.findTagById(tagId);
        if (tag.getUser().getId() != user.getId()) {
            throw new RuntimeException("无权修改此标签");
        }
        tag.setName(tagName);
        tagMapper.updateTagName(tag);
    }

    /**
     * 查询该user下的所有标签
     */
    public List<Tag> findTagByUserId() {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return tagMapper.findTagByUserId(user.getId());
//        return tagMapper.findAllTag();
    }

}
