package com.inspiration.config;

import com.inspiration.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author SunDocker
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 放行自定义登录接口
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf（后面具体讲）
                .csrf().disable()
                //不通过Session获取SecurityContext
                //前后端分离项目不要用Session了
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                // 对于登录、注册接口，只允许匿名访问，也就是认证之后(携带了token)就访问不了了
                .antMatchers("/login").anonymous()
                .antMatchers("/register").anonymous()
                // 部分功能需要鉴权认证
                .antMatchers("/users/currentUser").authenticated()
                .antMatchers("/user/logout").authenticated()
                .antMatchers("/comments/publish").authenticated()
                .antMatchers("/ideas/publish").authenticated()
                .antMatchers("/ideas/update").authenticated()
                .antMatchers("/users/collect").authenticated()
                .antMatchers("/users/cancelCollection").authenticated();
                //其他接口任意访问
        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
