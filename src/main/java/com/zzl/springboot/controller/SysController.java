package com.zzl.springboot.controller;

import com.zzl.springboot.common.Result;
import com.zzl.springboot.controller.dto.UserDTO;
import com.zzl.springboot.service.ISysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统操作控制器
 * @author TickNet-zzl
 * @date 2022/4/12  15:35
 */
@RestController
@RequestMapping(value = "/sys")
public class SysController {
    @Resource
    private ISysService sysService;

    @PostMapping("/loginByUserInfo")
    public Result login(@RequestBody UserDTO userDTO){
        return Result.success(sysService.login(userDTO));
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        return Result.success(sysService.register(userDTO));
    }
}
