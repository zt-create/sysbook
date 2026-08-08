package com.org.example.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class BookInfo {
    private int id;
    private String bookName;
    private String author;
    private String category;
    private String publishHouse;
    private int stock;
    private String bookDesc;
    private Date createTime;
    private Date updateTime;
    private int deleted;
}
