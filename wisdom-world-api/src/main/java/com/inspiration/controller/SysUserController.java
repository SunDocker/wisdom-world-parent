package com.inspiration.controller;

import com.inspiration.service.SysUserService;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.IdeaCollectParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author SunDocker
 */
@RestController
@RequestMapping("users")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser() {
        return sysUserService.currentUser();
    }

    @PostMapping("collect")
    public Result collect(@RequestBody IdeaCollectParam ideaCollectParam) {
        return sysUserService.collect(ideaCollectParam.getIdeaId());
    }

    @PostMapping("cancelCollection")
    public Result cancelCollection(@RequestBody IdeaCollectParam ideaCollectParam) {
        return sysUserService.cancelCollection(ideaCollectParam.getIdeaId());
    }

}
