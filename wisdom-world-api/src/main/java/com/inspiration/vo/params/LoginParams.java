package com.inspiration.vo.params;

import lombok.Data;

/**
 * @author SunDocker
 * 用于接收登录接口和注册接口的参数
 */
@Data
public class LoginParams {
    private String account;
    private String password;
    /**
     * nickname在注册时会用到
     */
    private String nickname;
}
