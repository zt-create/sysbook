package com.org.example.controller;

import com.org.example.pojo.SysUser;
import com.org.example.service.SysUserService;
import com.org.example.common.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    // 登录
    @PostMapping("/login")
    public Result<?> login(@RequestBody SysUser user) {
        SysUser userDB = sysUserService.login(user);
        if (userDB == null) {
            return Result.error("用户名或密码错误");
        } else {
            return Result.success(userDB);
        }
    }

    // 注册
    @PostMapping("/register")
    public Result<String> register(@RequestBody SysUser user) {
        sysUserService.register(user);
        return Result.success("注册成功");
    }

    // 查询所有用户
    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        return Result.success(sysUserService.list());
    }

    // 逻辑删除用户
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable int id) {
        sysUserService.delete(id);
        return Result.success("逻辑删除成功");
    }
}