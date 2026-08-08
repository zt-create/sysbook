package com.org.example.service;

import com.org.example.pojo.BookBorrow;

import java.util.List;

public interface BookBorrowService {
    //查询所有借阅记录
    List<BookBorrow> list();
    //添加借阅记录
    void add(BookBorrow bookBorrow);
    //归还图书
    void returnBook(int id);
    //根据id查询图书记录
    BookBorrow getById(int id);
}
