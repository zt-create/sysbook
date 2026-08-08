package com.org.example.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class BookBorrow {
    private int id;
    private int userId;
    private int bookId;
    private Date borrowTime;
    private Date returnTime;
    private String status;
}
