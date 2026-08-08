package com.org.example.service.Impl;

import com.org.example.mapper.SysUserMapper;
import com.org.example.pojo.SysUser;
import com.org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("/sysUserService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser login(SysUser user) {
        return sysUserMapper.login(user);
    }

    @Override
    public int register(SysUser user) {
      return    sysUserMapper.register(user);
    }

    @Override
    public List<SysUser> list() {
        return sysUserMapper.list();
    }

    @Override
    public SysUser getById(int id) {
        return sysUserMapper.getById(id);
    }

    @Override
    public int delete(int id) {
       return sysUserMapper.delete(id);
    }

    @Override
    public int update(SysUser user) {
       return sysUserMapper.update(user);
    }
}
