package com.yjb.controller;

import com.yjb.model.entity.PageResult;
import com.yjb.model.entity.Result;
import com.yjb.model.entity.StatusCode;
import com.yjb.model.pojo.Discuss;
import com.yjb.service.DiscussService;
import com.yjb.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Api(tags = "评论api", description = "评论api", basePath = "/discuss")
@RestController
@RequestMapping("/discuss")
public class DiscussController {

    @Autowired
    private DiscussService discussService;

    @ApiOperation(value = "发布评论", notes = "评论内容+博文id")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{blogId}")
    public Result discuss(String discussBody, @PathVariable Long blogId) {
        if (!FormatUtil.checkStringNull(discussBody))
            return Result.create(StatusCode.ERROR, "参数错误");
        if (!FormatUtil.checkPositive(blogId))
            return Result.create(StatusCode.ERROR, "参数错误");

        discussService.saveDiscuss(discussBody, blogId);
        return Result.create(StatusCode.OK, "评论成功");
    }

    @ApiOperation(value = "删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{discussId}")
    public Result deleteDiscuss(@PathVariable Long discussId) {
        if (!FormatUtil.checkPositive(discussId))
            return Result.create(StatusCode.ERROR, "参数错误");
        try {
            discussService.deleteDiscuss(discussId);
            return Result.create(StatusCode.OK, "删除评论成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }

    }

    @ApiOperation(value = "管理员删除评论", notes = "评论id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{discussId}")
    public Result adminDeleteDiscuss(@PathVariable Long discussId) {
        if (!FormatUtil.checkPositive(discussId))
            return Result.create(StatusCode.ERROR, "参数错误");

        try {
            discussService.adminDeleteDiscuss(discussId);
            return Result.create(StatusCode.OK, "删除评论成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        }

    }

    @ApiOperation(value = "分页查询博文评论以及回复", notes = "博文id+页码+显示数")
    @GetMapping("/{blogId}/{page}/{showCount}")
    public Result getDiscussByBlog(@PathVariable Long blogId,
                                   @PathVariable Integer page,
                                   @PathVariable Integer showCount) {

        if (!FormatUtil.checkPositive(page, showCount) || !FormatUtil.checkPositive(blogId))
            return Result.create(StatusCode.ERROR, "参数错误");
        PageResult<Discuss> pageResult = new PageResult(discussService.getDiscussCountByBlogId(blogId), discussService.findDiscussByBlogId(blogId, page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "首页获取最新评论", notes = "获取最新六条评论")
    @GetMapping("/newDiscuss")
    public Result newDiscuss() {
        return Result.create(StatusCode.OK, "查询成功", discussService.findNewDiscuss());
    }

    @ApiOperation(value = "获取用户发布的所有博文下的评论", notes = "获取用户发布的所有博文下的评论")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/userNewDiscuss")
    public Result userNewDiscuss() {
        return Result.create(StatusCode.OK, "查询成功", discussService.findUserNewDiscuss());
    }

}
