package com.org.example.controller;

import com.org.example.common.Result;
import com.org.example.pojo.BookInfo;
import com.org.example.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookInfoController {

    @Autowired
    private BookInfoService bookInfoService;

    // 查询图书列表（支持按名称模糊查询）
    @GetMapping("/list")
    public Result<List<BookInfo>> list(String bookName) {
        List<BookInfo> list = bookInfoService.list(bookName);
        return Result.success(list);
    }
    //根据id查图书信息
    @GetMapping("/get/{id}")
    public Result<BookInfo> getById(@PathVariable int id) {
        BookInfo book = bookInfoService.getById(id);
        return Result.success(book);
    }

    // 添加图书
    @PostMapping("/add")
    public Result<String> add(@RequestBody BookInfo bookInfo) {
        bookInfoService.add(bookInfo);
        return Result.success("添加成功");
    }

    // 修改图书信息
    @PutMapping("/update")
    public Result<String> update(@RequestBody BookInfo bookInfo) {
        bookInfoService.update(bookInfo);
        return Result.success("修改成功");
    }

    // 逻辑删除图书
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable int id) {
        bookInfoService.delete(id);
        return Result.success("逻辑删除成功");
    }
}