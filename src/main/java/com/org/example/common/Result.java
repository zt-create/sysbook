package com.org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一返回结果类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data){
        return new Result<>(200,"操作成功",data);
    }

    public static <T> Result<T> error(String msg){
        return new Result<>(500,msg,null);
    }

}
