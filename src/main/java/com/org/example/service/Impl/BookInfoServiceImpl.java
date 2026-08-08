package com.org.example.service.Impl;

import com.org.example.mapper.BookInfoMapper;
import com.org.example.pojo.BookInfo;
import com.org.example.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("/bookInfoService")
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Override
    public List<BookInfo> list(String bookName) {
        return bookInfoMapper.list(bookName);
    }

    @Override
    public BookInfo getById(int id) {
        return bookInfoMapper.getById(id);
    }

    @Override
    public void add(BookInfo bookInfo) {
        bookInfoMapper.add(bookInfo);
    }

    @Override
    public void update(BookInfo bookInfo) {
        bookInfoMapper.update(bookInfo);
    }

    @Override
    public void delete(int id) {
        bookInfoMapper.delete(id);
    }

    @Override
    public void updateStock(BookInfo bookInfo) {
        bookInfoMapper.updateStock(bookInfo);
    }
}
