package com.inspiration.handler;

import com.alibaba.fastjson.JSON;
import com.inspiration.util.WebUtils;
import com.inspiration.vo.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SunDocker
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String msg;
        if (authException instanceof BadCredentialsException) {
            msg = "用户名或密码错误";
        } else if (authException instanceof InsufficientAuthenticationException) {
            msg = "请登录后再进行此操作";
        } else {
            msg = authException.getMessage();
        }
        Result result = Result.fail(502, msg);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}