package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspiration.dao.mapper.SysUserMapper;
import com.inspiration.dao.pojo.SysUser;
import com.inspiration.service.SysUserService;
import com.inspiration.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author SunDocker
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, username);
        queryWrapper.select(SysUser::getAccount, SysUser::getPassword, SysUser::getAvatar, SysUser::getNickname, SysUser::getId);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (Objects.isNull(sysUser)) {
            throw new RuntimeException("用户名错误");
        }
        return new LoginUser(sysUser);
    }
}
