package com.nhnacademy.shop.wrap.exception;
/**
 * 포장 아이디가 존재하지 않을 때 발생하는 Exception 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-29
 **/
public class NotFoundWrapException extends RuntimeException{
    public NotFoundWrapException(Long wrapId){
        super("not found" + wrapId);
    }
}
