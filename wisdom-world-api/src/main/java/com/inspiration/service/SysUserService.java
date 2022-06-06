package com.inspiration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspiration.dao.pojo.SysUser;
import com.inspiration.vo.Result;

import java.util.List;

/**
 * @author SunDocker
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 返回当前用户
     * @return
     */
    Result currentUser();

    /**
     * 用户收藏对应id的idea
     * @param ideaId
     * @return
     */
    Result collect(Long ideaId);

    /**
     * 用户取消收藏对应的id
     * @param ideaId
     * @return
     */
    Result cancelCollection(Long ideaId);
}
