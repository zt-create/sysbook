package com.org.example.service;

import com.org.example.pojo.BookInfo;

import java.util.List;

public interface BookInfoService {
    //根据图书名查询图书信息
    List<BookInfo> list(String bookName);
    //根据id查询
    BookInfo getById(int id);
    //添加图书信息
    void add(BookInfo bookInfo);
    //修改图书信息
    void update(BookInfo bookInfo);
    //逻辑删除图书
    void delete(int id);
    //更新库存
    void updateStock(BookInfo bookInfo);
}
