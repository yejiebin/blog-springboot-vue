package com.yjb.service;

import com.yjb.mapper.*;
import com.yjb.model.pojo.Blog;
import com.yjb.model.pojo.Discuss;
import com.yjb.model.pojo.User;
import com.yjb.utils.DateUtil;
import com.yjb.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussMapper discussMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询热门博文
     * 正常状态
     *
     * @return
     */
    public List<Blog> findHotBlog() {
        return blogMapper.findHotBlog(6);
    }

    /**
     * 按月份归档博客
     * 正常状态
     *
     * @return
     */
    public List<Map> statisticalBlogByMonth() {
        return blogMapper.statisticalBlogByMonth(6);
    }

    /**
     * 查询主页所有博客数量
     * 正常状态
     *
     * @return
     */
    public Long getHomeBlogCount() {
        return blogMapper.getHomeBlogCount();
    }

    /**
     * 查询主页博客
     * 正常状态
     *
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> findHomeBlog(Integer page, Integer showCount) {
        List<Blog> blogs = blogMapper.findHomeBlog((page - 1) * showCount, showCount);
        for (Blog blog : blogs) {
            blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 搜索博文
     * 正常状态
     *
     * @param searchText
     * @return
     */
    public List<Blog> searchBlog(String searchText, Integer page, Integer showCount) {
        List<Blog> blogs = blogMapper.searchBlog(searchText, (page - 1) * showCount, showCount);
        for (Blog blog : blogs) {
            blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }

    /**
     * 符合关键词的博文数量
     * 正常状态
     *
     * @param searchText
     * @return
     */
    public Long getSearchBlogCount(String searchText) {
        return blogMapper.getSearchBlogCount(searchText);
    }

    /**
     * 根据id查询博文以及博文标签
     * 正常状态
     *
     * @param blogId
     * @return
     */
    public Blog findBlogById(Long blogId) {
       Blog blog = blogMapper.findBlogById(blogId);
       if (blog == null) {
           throw new RuntimeException("此博客不存在");
       }
       blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
       blog.setBlogViews(blog.getBlogViews()+1);
       blogMapper.updateBlogViews(blog);
       return blog;
    }

    /**
     * 修改博文
     *
     * @param blogId
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    @Transactional
    public void updateBlog(Long blogId, String blogTitle, String blogBody, Long[] tagIds) {
        //判断是否是当前用户
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogMapper.findBlogById(blogId);
        if (user.getId() != blog.getUser().getId()) {
            throw new RuntimeException("您没有权限修改");
        }
        blog.setTitle(blogTitle);
        blog.setBody(blogBody);
        blogMapper.updateBlog(blog);
        //删除原有的标签
        tagMapper.deleteTagByBlogId(blog.getId());
        //保存新标签
        for (Long tagId : tagIds) {
            blogMapper.saveBlogTag(blog.getId(), tagId);
        }
    }

    /**
     * 用户删除博文
     *
     * @param blogId
     */
    @Transactional
    public void deleteBlog(Long blogId) {
        //判断是否是当前用户
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = blogMapper.findBlogById(blogId);
        if (user.getId() != blog.getUser().getId()) {
            throw new RuntimeException("您没有权限修改");
        }
        blogMapper.updateBlogState(blogId, 0);   //更改博客状态
        //级联删除标签
        tagMapper.deleteTagByBlogId(blogId);
        //级联删除评论和回复
        List<Discuss> discusses = discussMapper.findDiscussByBlog(blog);
        for (Discuss discuss : discusses) {
            replyMapper.deleteReplyByDiscussId(discuss.getId());
            discussMapper.deleteDiscussById(discuss.getId());
        }
    }

    /**
     * 保存博文
     *
     * @param blogTitle
     * @param blogBody
     * @param tagIds
     */
    @Transactional
    public void saveBlog(String blogTitle, String blogBody, Long[] tagIds) {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Blog blog = new Blog();
        blog.setUser(user);//博文用户
        blog.setBlogViews(0);//浏览量
        blog.setDiscussCount(0);//评论数
        blog.setTitle(blogTitle);//标题
        blog.setBody(blogBody);//内容
        blog.setState(1);//1 正常状态
        blog.setTime(DateUtil.getCurrentDate());//发布时间
        blogMapper.saveBlog(blog);
        for (Long tagId : tagIds) {
            blogMapper.saveBlogTag(blog.getId(), tagId);
        }
    }

    /**
     * 返回博客的所有数量（包括删除状态）
     * @return
     */
    public Long getAllBlogCount() {
        return blogMapper.getAllBlogCount();
    }

    /**
     * 分页查询所有博客
     * @param page
     * @param showCount
     * @return
     */
    public List<Blog> findAllBlog(Integer page, Integer showCount) {
        return blogMapper.findAllBlog((page-1)*showCount, showCount);
    }

    /**
     * 管理员删除博客(级联删除标签)
     * @param blogId
     */
    @Transactional
    public void adminDeleteBlog(Long blogId) {
        blogMapper.updateBlogState(blogId, 0);
        tagMapper.deleteTagByBlogId(blogId);
        Blog blog = new Blog();
        blog.setId(blogId);
        //级联删除评论和回复
        List<Discuss> discusses = discussMapper.findDiscussByBlog(blog);
        for (Discuss discuss : discusses) {
            replyMapper.deleteReplyByDiscussId(discuss.getId());
            discussMapper.deleteDiscussById(discuss.getId());
        }

    }

    /**
     * 管理员分页模糊查询
     * @param searchText
     * @return
     */
    public Long getSearchAllBlogCount(String searchText) {
        return blogMapper.getSearchAllBlogCount(searchText);
    }

    /**
     * 管理员分页模糊查询所有博客
     * @param searchText
     * @param page
     * @param showCount
     * @return
     */
    public List searchAllBlog(String searchText, Integer page, Integer showCount) {
        return blogMapper.searchAllBlog(searchText, (page-1)*showCount, showCount);
    }

    public Long getBlogCountByUser() {
        User user = userMapper.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return blogMapper.getBlogCountByUser(user);
    }

    public List<Blog> findBlogByUser(Integer page, Integer showCount) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userMapper.findUserByName(username);
        List<Blog> blogs = blogMapper.findBlogByUser(user, (page-1)*showCount, showCount);
        for (Blog blog : blogs) {
            blog.setTags(tagMapper.findTagByBlogId(blog.getId()));
        }
        return blogs;
    }
}
