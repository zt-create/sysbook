package com.org.example.controller;

import com.org.example.common.Result;
import com.org.example.pojo.BookBorrow;
import com.org.example.pojo.BookInfo;
import com.org.example.service.BookBorrowService;
import com.org.example.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BookBorrowController {
    @Autowired
    private BookBorrowService bookBorrowService;
    @Autowired
    private BookInfoService bookInfoService;

    @GetMapping("/list")
    public Result<List<BookBorrow>> list(){
        return Result.success(bookBorrowService.list());
    }


    @PostMapping("/borrow")
    public Result<?> borrow(@RequestBody BookBorrow bookBorrow){
        BookInfo bookInfo = bookInfoService.getById(bookBorrow.getBookId());
        if (bookInfo.getStock()<=0){
            return Result.error( "库存不足，不能借阅");
        }else {
            bookInfo.setStock(bookInfo.getStock()-1);
            bookInfoService.updateStock(bookInfo);
            bookBorrowService.add(bookBorrow);
            return Result.success("借阅成功");
        }
    }

    @PostMapping("/back/{id}")
    public Result back(@PathVariable int id){
        BookBorrow borrowRecord = bookBorrowService.getById(id);
        BookInfo bookInfo = bookInfoService.getById(borrowRecord.getBookId());
        bookInfo.setStock(bookInfo.getStock()+1);
        bookInfoService.updateStock(bookInfo);
        bookBorrowService.returnBook(id);
        return Result.success("归还成功");
    }
}
