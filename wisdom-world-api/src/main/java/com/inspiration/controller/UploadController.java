package com.inspiration.controller;

import com.inspiration.service.UploadService;
import com.inspiration.util.QiniuUtils;
import com.inspiration.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author SunDocker
 */
@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    UploadService uploadService;

    @PostMapping
    public Result uploadImage(MultipartFile image) {
        return uploadService.uploadImage(image);
    }
}
