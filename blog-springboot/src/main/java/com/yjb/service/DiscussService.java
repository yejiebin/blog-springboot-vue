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
import java.util.List;

@Service
public class DiscussService {

    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取首页最新的评论
     *
     * @return
     */
    public List<Discuss> findNewDiscuss() {
        return discussMapper.findNewDiscuss(6);
    }

    /**
     * 获取用户发布的所有博文下的评论
     *
     * @return
     */
    public List<Discuss> findUserNewDiscuss() {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return discussMapper.findUserNewDiscuss(user.getId(), 6);
    }

    /**
     * 获取博文下评论数量
     *
     * @param blogId
     * @return
     */
    public Long getDiscussCountByBlogId(Long blogId) {
        return discussMapper.getDiscussCountByBlogId(blogId);
    }

    /**
     * 根据博文id查询 该博文下的评论及回复
     *
     * @param blogId
     * @return
     */
    public List<Discuss> findDiscussByBlogId(Long blogId, Integer page, Integer showCount) {

        List<Discuss> discusses = discussMapper.findDiscussByBlogId(blogId, (page-1)*showCount, showCount);

        for (Discuss discuss : discusses) {
            List<Reply> replyList = replyMapper.findReplyByDiscussId(discuss.getId());
            for (Reply reply : replyList) {
                if (reply.getReply() != null) {
                    reply.setReply(replyMapper.findReplyById(reply.getReply().getId()));
                }
            }
            discuss.setReplyList(replyList);
        }

        return discusses;
    }

    /**
     * 发布评论
     * 博文评论数加1
     *
     * @param discussBody
     * @param blogId
     */
    @Transactional
    public void saveDiscuss(String discussBody, Long blogId) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogMapper.findBlogById(blogId);
        Discuss discuss = new Discuss();
        discuss.setBody(discussBody);
        discuss.setBlog(blog);
        discuss.setUser(user);
        discuss.setTime(DateUtil.getCurrentDate());
        discussMapper.saveDiscuss(discuss);
        //评论数加一
        blog.setDiscussCount(blog.getDiscussCount()+1);
        blogMapper.updateBlogDiscussCount(blog);
    }

    /**
     * 删除评论
     * 级联删除评论下的所有回复
     * 博文评论数 - (评论数+回复数)
     *
     * @param discussId
     */
    @Transactional
    public void deleteDiscuss(Long discussId) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Discuss discuss = discussMapper.findDiscussById(discussId);
        if (discuss == null)
            throw new RuntimeException("评论不存在");
        if (user.getId() != discuss.getUser().getId())
            throw new RuntimeException("无权删除");

        discussMapper.deleteDiscussById(discuss.getId());

        Long rows = replyMapper.deleteReplyByDiscussId(discuss.getId());    //返回所受影响的行数
        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setDiscussCount(blog.getDiscussCount()-1-rows.intValue());
        blogMapper.updateBlogDiscussCount(blog);
    }

    /**
     * 管理员删除评论
     * 级联删除评论下的所有回复
     * 博文评论数 - (评论数+回复数)
     * @param discussId
     */
    public void adminDeleteDiscuss(Long discussId) {
        Discuss discuss = discussMapper.findDiscussById(discussId);
        if (discuss == null)
            throw new RuntimeException("评论不存在");
        discussMapper.deleteDiscussById(discussId);

        Long rows = replyMapper.deleteReplyByDiscussId(discussId); //返回受影响行数

        Blog blog = blogMapper.findBlogById(discuss.getBlog().getId());
        blog.setDiscussCount(blog.getDiscussCount() - 1 - rows.intValue());
        blogMapper.updateBlogDiscussCount(blog);
    }
}
