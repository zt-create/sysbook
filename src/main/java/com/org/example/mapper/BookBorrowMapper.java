package com.org.example.mapper;

import com.org.example.pojo.BookBorrow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookBorrowMapper {
    List<BookBorrow> list();
    void add(BookBorrow bookBorrow);
    void returnBook(int id);
    BookBorrow getById(int id);
}
