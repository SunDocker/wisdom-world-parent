package com.inspiration.controller;

import com.inspiration.service.LoginService;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author SunDocker
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    public Result login(@RequestBody LoginParams loginParams) {
        return loginService.login(loginParams);
    }

    @GetMapping("user/logout")
    public Result logout() {
        return loginService.logout();
    }

    @PostMapping("register")
    public Result register(@RequestBody LoginParams registerParams) {
        return loginService.register(registerParams);
    }
}
