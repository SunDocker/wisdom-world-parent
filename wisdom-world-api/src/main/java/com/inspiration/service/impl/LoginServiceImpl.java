package com.inspiration.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inspiration.dao.pojo.SysUser;
import com.inspiration.service.LoginService;
import com.inspiration.service.SysUserService;
import com.inspiration.util.JwtUtils;
import com.inspiration.vo.ErrorCode;
import com.inspiration.vo.LoginUser;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.LoginParams;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author SunDocker
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result login(LoginParams loginParams) {
        //1.检查参数是否合法
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //2.根据用户名和密码去user表中查询是否存在且是否合法
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //3.如果不合法，则登录失败（用户名不存在是在下层检验的）
        if (Objects.isNull(authenticate)) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //4.如果存在，则使用JWT生成Token：userId expiration，返回给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        Long userId = sysUser.getId();
        String token = JwtUtils.createToken(userId);
        //5.再将Token存入Redis中一份，相当于双重保险，防止未登录者伪造Token
        redisTemplate.opsForValue().set("login:" + userId, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        if (Boolean.TRUE.equals(redisTemplate.delete("login:" + sysUser.getId()))) {
            System.out.println("后端登出成功了");
            return Result.success(null);
        }
        System.out.println("后端登出失败了");
        return Result.fail(500, "退出登录失败");
    }

    @Override
    public Result register(LoginParams registerParams) {
        String account = registerParams.getAccount();
        String password = registerParams.getPassword();
        String nickname = registerParams.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if (Objects.nonNull(sysUser)) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(password));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/user/user_1.png");
        //1 为true
        sysUser.setAdmin(1);
        // 0 为false
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        if (this.sysUserService.save(sysUser)) {
            Long userId = sysUser.getId();
            String token = JwtUtils.createToken(userId);
            redisTemplate.opsForValue().set("login:" + userId, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
            return Result.success(token);
        }
        return Result.fail(500, "注册失败");
    }
}
