package com.yjb.controller;

import com.yjb.config.SiteIntroductionConfig;
import com.yjb.model.entity.Result;
import com.yjb.model.entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Api(tags = "站点api", description = "站点api", basePath = "/site")
@RestController
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteIntroductionConfig siteIntroductionConfig;

    @ApiOperation(value = "站点介绍", notes = "站点介绍")
    @GetMapping
    public Result getIntroduction() {
        return Result.create(StatusCode.OK, "获取成功", siteIntroductionConfig.getIntroduction());
    }

}
