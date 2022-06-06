package com.inspiration.service;

import com.inspiration.vo.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author SunDocker
 */
public interface UploadService {
    /**
     * 上传图片到七牛云
     * @param image
     * @return
     */
    Result uploadImage(MultipartFile image);
}
