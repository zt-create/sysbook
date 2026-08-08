package com.org.example.pojo;

import lombok.Data;

import java.util.Date;

@Data

public class SysUser {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String headImg;
    private String role;
    private Date createTime;
    private Date updateTime;
    private int deleted;
}
