package com.nhnacademy.shop.wrap.exception;
/**
 * 포장 이름이 존재하지 않을 때 발생하는 Exception 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-29
 **/
public class NotFoundWrapNameException extends RuntimeException{
    public NotFoundWrapNameException(String wrapName){
        super("not found "+ wrapName);
    }
}
