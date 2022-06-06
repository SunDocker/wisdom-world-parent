package com.inspiration.filter;

import com.alibaba.fastjson.JSON;
import com.inspiration.dao.pojo.SysUser;
import com.inspiration.util.JwtUtils;
import com.inspiration.util.WebUtils;
import com.inspiration.vo.LoginUser;
import com.inspiration.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author SunDocker
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //获取token
        String token = request.getHeader("Authorization");
        //如果还没有token，说明是没登录，此时是匿名状态，直接放行即可
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            //这里要return是因为响应回来的时候还会回到doFilter方法的下一行
            return;
        }
        //解析token
        String userid;
        try {
            userid = JwtUtils.checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            Result result = Result.fail(500, "token非法");
            String json = JSON.toJSONString(result);
            WebUtils.renderString(response, json);
            return;
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        String sysUserJson = redisTemplate.opsForValue().get(redisKey);
        SysUser sysUser = JSON.parseObject(sysUserJson, SysUser.class);
        //redis中没有该用户，但token合法，说明有可能是未登录但伪造了token，或者是退出登录了但没清除token
        if(Objects.isNull(sysUser)){
            Result result = Result.fail(500, "用户未登录");
            String json = JSON.toJSONString(result);
            WebUtils.renderString(response, json);
            return;
        }
        //存入SecurityContextHolder，这是为了给后面的过滤器确定认证状态
        //使用三个参数的，是为了给用户设置已认证状态(三个参数的方法体中有设置)
        //TODO 获取权限信息封装到Authentication中
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(new LoginUser(sysUser),null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //调用这个方法就会放行请求
        filterChain.doFilter(request, response);
    }
}
