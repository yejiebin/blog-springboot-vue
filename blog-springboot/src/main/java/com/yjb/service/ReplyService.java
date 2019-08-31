package com.yjb.service;

import com.yjb.mapper.BlogMapper;
import com.yjb.mapper.DiscussMapper;
import com.yjb.mapper.ReplyMapper;
import com.yjb.mapper.UserMapper;
import com.yjb.model.pojo.Blog;
import com.yjb.model.pojo.Discuss;
import com.yjb.model.pojo.Reply;
import com.yjb.model.pojo.User;
import com.yjb.utils.DateUtil;
import com.yjb.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 保存回复
     *
     * @param discussId
     * @param replyBody
     * @param rootId    可为null
     */
    @Transactional
    public void saveReply(Long discussId, String replyBody, Long rootId) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Discuss discuss = discussMapper.findDiscussById(discussId);
        if (discuss == null) {
            throw new RuntimeException("评论不存在");
        }
        Reply reply = new Reply();
        reply.setBody(replyBody);
        reply.setDiscuss(discuss);
        Reply rootReply = new Reply();
        rootReply.setId(rootId);
        reply.setReply(rootReply);
        reply.setUser(user);
        reply.setTime(DateUtil.getCurrentDate());
        replyMapper.saveReply(reply);

        //博客评论数+1
        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setDiscussCount(blog.getDiscussCount()+1);
        blogMapper.updateBlogDiscussCount(blog);
    }

    /**
     * 删除回复
     * 博客评论数-1
     * @param replyId
     */
    @Transactional
    public void deleteReply(Long replyId) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Reply reply = replyMapper.findReplyById(replyId);
        if (reply == null)
            throw new RuntimeException("回复不存在");

        if (user.getId() != reply.getUser().getId())
            throw new RuntimeException("无权删除");

        //删除回复
        replyMapper.deleteReplyById(replyId);
        //博客评论数-1
        Discuss discuss = discussMapper.findDiscussById(reply.getDiscuss().getId());
        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setDiscussCount(blog.getDiscussCount()-1);
        blogMapper.updateBlogDiscussCount(blog);
    }

    /**
     * 管理员删除回复
     * 博客评论数-1
     * @param replyId
     */
    @Transactional
    public void adminDeleteReply(Long replyId) {
        Reply reply = replyMapper.findReplyById(replyId);
        if (reply == null)
            throw new RuntimeException("回复不存在");

        //删除回复
        replyMapper.deleteReplyById(replyId);
        //博客评论数-1
        Discuss discuss = discussMapper.findDiscussById(reply.getDiscuss().getId());
        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setDiscussCount(blog.getDiscussCount()-1);
        blogMapper.updateBlogDiscussCount(blog);
    }
}
