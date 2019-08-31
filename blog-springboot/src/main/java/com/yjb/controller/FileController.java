package com.yjb.controller;

import com.yjb.model.entity.Result;
import com.yjb.model.entity.StatusCode;
import com.yjb.provider.UCloudProvider;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@Api(tags = "文件api", description = "文件api", basePath = "/file")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            return Result.create(StatusCode.OK, "上传成功", fileName);
        } catch (Exception e) {
            return Result.create(StatusCode.OK, "上传失败", e.getMessage());
        }
    }

}
