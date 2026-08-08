package com.org.example.service;

import com.org.example.pojo.SysUser;

import java.util.List;

public interface SysUserService {
    //登录
    SysUser login(SysUser user);
    //注册
    int register(SysUser user);
    //查询所有
    List<SysUser> list();
    //查询某用户
    SysUser getById(int id);
    //逻辑删除
    int delete(int id);
    //修改用户信息
    int update(SysUser user);
}
