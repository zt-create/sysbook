package com.org.example.service.Impl;

import com.org.example.mapper.BookBorrowMapper;
import com.org.example.pojo.BookBorrow;
import com.org.example.service.BookBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("/bookBorrowService")
public class BookBorrowServiceImpl implements BookBorrowService {

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Override
    public List<BookBorrow> list() {
        return bookBorrowMapper.list();
    }

    @Override
    public void add(BookBorrow bookBorrow) {
        bookBorrowMapper.add(bookBorrow);
    }

    @Override
    public void returnBook(int id) {
        bookBorrowMapper.returnBook(id);
    }

    @Override
    public BookBorrow getById(int id) {
        return bookBorrowMapper.getById(id);
    }
}
