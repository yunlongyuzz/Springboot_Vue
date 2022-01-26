package com.yyl.myblog.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    private String result;

    private String message;

    private T data;

    //成功返回
    public static <Type> ResultEntity<Type> successWithoutData(){
        return new ResultEntity<Type>(SUCCESS,"操作成功",null);
    }

    //成功返回带数据
    public static <Type> ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<Type>(SUCCESS,"操作成功",data);
    }



    //返回失败
    public static <Type> ResultEntity<Type> failed(String message){
        return new ResultEntity<Type>(FAILED,message,null);
    }

    //返回失败带数据
    public static <Type> ResultEntity<Type> failedWithData(String message,Type data){
        return new ResultEntity<Type>(FAILED,message,data);
    }



}
