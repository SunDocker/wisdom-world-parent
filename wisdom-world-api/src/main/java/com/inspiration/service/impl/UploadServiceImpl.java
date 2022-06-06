package com.inspiration.service.impl;

import com.inspiration.service.UploadService;
import com.inspiration.util.QiniuUtils;
import com.inspiration.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author SunDocker
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    QiniuUtils qiniuUtils;
    @Override
    public Result uploadImage(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String imageName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        boolean uploadSuccess = qiniuUtils.upload(image, imageName);
        if (uploadSuccess) {
            return Result.success(QiniuUtils.url + imageName);
        } else {
            return Result.fail(20001, "上传失败");
        }
    }
}
