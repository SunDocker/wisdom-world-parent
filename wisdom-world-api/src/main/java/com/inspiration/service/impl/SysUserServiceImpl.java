package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspiration.dao.mapper.SysUserMapper;
import com.inspiration.dao.pojo.IdeaCollection;
import com.inspiration.dao.pojo.SysUser;
import com.inspiration.service.IdeaCollectionService;
import com.inspiration.service.SysUserService;
import com.inspiration.vo.CurrentUserVo;
import com.inspiration.vo.LoginUser;
import com.inspiration.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author SunDocker
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private IdeaCollectionService ideaCollectionService;

    @Override
    public Result currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        CurrentUserVo currentUserVo = new CurrentUserVo();
        BeanUtils.copyProperties(sysUser, currentUserVo);
        return Result.success(currentUserVo);
    }

    @Override
    public Result collect(Long ideaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        IdeaCollection ideaCollection = new IdeaCollection();
        ideaCollection.setUid(sysUser.getId());
        ideaCollection.setIid(ideaId);
        System.out.println("====================>" + ideaCollection);
        if (ideaCollectionService.save(ideaCollection)) {
            return Result.success("收藏成功");
        }
        return Result.fail(520, "数据插入异常");
    }

    @Override
    public Result cancelCollection(Long ideaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        LambdaQueryWrapper<IdeaCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdeaCollection::getUid, sysUser.getId());
        queryWrapper.eq(IdeaCollection::getIid, ideaId);
        if (ideaCollectionService.remove(queryWrapper)) {
            return Result.success("取消收藏成功");
        }
        return Result.fail(520, "数据删除异常");
    }
}
