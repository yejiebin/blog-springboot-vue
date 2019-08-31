package com.yjb.controller;

import com.yjb.model.entity.PageResult;
import com.yjb.model.entity.Result;
import com.yjb.model.entity.StatusCode;
import com.yjb.model.pojo.Blog;
import com.yjb.service.BlogService;
import com.yjb.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Api(tags = "博文api", description = "博文api", basePath = "/blog")
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @ApiOperation(value = "根据id查询博文", notes = "博文id")
    @GetMapping("/{blogId}")
    public Result findBlogById(@PathVariable Long blogId) {
        if (!FormatUtil.checkObjectNull(blogId)) {
            return Result.create(StatusCode.ERROR, "参数有误");
        }
        try {
            return Result.create(StatusCode.OK, "查询成功", blogService.findBlogById(blogId));
        } catch (RuntimeException e) {
            return Result.create(StatusCode.NOTFOUND, "此博客不存在");
        }
    }

    /**
     * 根据用户分页查询博文
     *
     * @param page      页数
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "根据用户分页查询博文", notes = "页数+显示数量")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myblog/{page}/{showCount}")
    public Result findBlogByUser(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!FormatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.OK, "参数错误");

        PageResult<Blog> pageResult =
                new PageResult(blogService.getBlogCountByUser(), blogService.findBlogByUser(page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 热门博文
     * 正常状态
     *
     * @return
     */
    @ApiOperation(value = "首页热门博文", notes = "首页热门博文")
    @GetMapping("/hotBlog")
    public Result hotBlog() {
        return Result.create(StatusCode.OK, "查询成功", blogService.findHotBlog());
    }

    @ApiOperation(value = "按月份归档博客", notes = "按月份归档博客")
    @GetMapping("/statisticalBlogByMonth")
    public Result statisticalBlogByMonth() {
        return Result.create(StatusCode.OK, "查询成功", blogService.statisticalBlogByMonth());
    }

    /**
     * 首页分页查询
     *
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "首页分页查询博文", notes = "页数+显示数量")
    @GetMapping("/home/{page}/{showCount}")
    public Result homeBlog(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!FormatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.OK, "参数有误");
        }
        PageResult<Blog> pageResult = new PageResult<>(blogService.getHomeBlogCount(), blogService.findHomeBlog(page, showCount));
        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "修改博文", notes = "博文id+博文标题+博文内容+博文标签")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{blogId}")
    public Result updateBlog(@PathVariable("blogId") Long blogId, String blogTitle, String blogBody, Long[] tagId) {
        if (!FormatUtil.checkStringNull(blogTitle, blogBody))
            return Result.create(StatusCode.ERROR, "参数错误");

        if (!FormatUtil.checkPositive(tagId) || !FormatUtil.checkPositive(blogId.intValue()))
            return Result.create(StatusCode.ERROR, "参数错误");

        try {
            blogService.updateBlog(blogId, blogTitle, blogBody, tagId);
            return Result.create(StatusCode.OK, "修改成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "修改失败" + e.getMessage());
        }
    }

    /**
     * 保存博文，博文内容由前端md编辑器生成
     *
     * @param blogBody
     * @param blogTitle
     *
     * @return
     */
    @ApiOperation(value = "发布博文", notes = "博文标题+博文内容+博文标签")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public Result newBlog(String blogTitle, String blogBody, Long[] tagId) {
        if (!FormatUtil.checkStringNull(blogTitle, blogBody) || !FormatUtil.checkPositive(tagId))
            return Result.create(StatusCode.ERROR, "参数错误");

        blogService.saveBlog(blogTitle, blogBody, tagId);
        return Result.create(StatusCode.OK, "发布成功");
    }

    @ApiOperation(value = "用户删除博文", notes = "博文id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{blogId}")
    public Result deleteBlog(@PathVariable Long blogId) {
        if (!FormatUtil.checkPositive(blogId.intValue()))
            return Result.create(StatusCode.ERROR, "参数错误");

        try {
            blogService.deleteBlog(blogId);
            return Result.create(StatusCode.OK, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }
    }

    /**
     * 博文搜索
     * 正常状态
     *
     * @param search
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页搜索博文", notes = "搜索内容+页码+显示条数")
    @GetMapping("/searchBlog/{page}/{showCount}")
    public Result searchBlog(String search,
                             @PathVariable Integer page,
                             @PathVariable Integer showCount) {
        if (!FormatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.OK, "参数错误");
        if (!FormatUtil.checkStringNull(search))
            return Result.create(StatusCode.OK, "参数错误");

        PageResult<Blog> pageResult = new PageResult(blogService.getSearchBlogCount(search),
                blogService.searchBlog(search, page, showCount));
        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 查询所有博客，包括删除状态
     *
     * @return
     */
    @ApiOperation(value = "管理员查询博文", notes = "管理员查询博文")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/AllBlog/{page}/{showCount}")
    public Result findAllBlog(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!FormatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.ERROR, "参数错误");
        PageResult<Blog> pageResult = new PageResult(blogService.getAllBlogCount(), blogService.findAllBlog(page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "管理员删除博文", notes = "博文id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{blogId}")
    public Result adminDeleteBlog(@PathVariable Long blogId) {
        if (!FormatUtil.checkPositive(blogId))
            return Result.create(StatusCode.ERROR, "参数错误");
        blogService.adminDeleteBlog(blogId);
        return Result.create(StatusCode.OK, "删除成功");
    }

    @ApiOperation(value = "管理员分页搜索博文", notes = "搜索内容+页码+显示条数")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/searchAllBlog/{page}/{showCount}")
    public Result searchAllBlog(String searchText,
                                @PathVariable Integer page,
                                @PathVariable Integer showCount) {

        if (!FormatUtil.checkPositive(page, showCount))
            return Result.create(StatusCode.OK, "参数错误");
        if (!FormatUtil.checkStringNull(searchText))
            return Result.create(StatusCode.OK, "参数错误");

        PageResult<Blog> pageResult = new PageResult(blogService.getSearchAllBlogCount(searchText),
                blogService.searchAllBlog(searchText, page, showCount));


        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

}
