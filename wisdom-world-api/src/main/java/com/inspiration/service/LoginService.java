package com.inspiration.service;

import com.inspiration.vo.Result;
import com.inspiration.vo.params.LoginParams;

/**
 * @author SunDocker
 */
public interface LoginService {
    /**
     * 登录功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 退出登录
     * @return
     */
    Result logout();

    /**
     * 注册
     * @param registerParams
     * @return
     */
    Result register(LoginParams registerParams);
}
