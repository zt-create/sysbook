package com.org.example.mapper;

import com.org.example.pojo.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookInfoMapper {
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
