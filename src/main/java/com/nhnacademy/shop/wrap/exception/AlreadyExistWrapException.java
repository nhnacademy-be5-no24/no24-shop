package com.nhnacademy.shop.wrap.exception;

/**
 * 포장이 이미 존재할 때 발생하는 Exception 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-29
 **/
public class AlreadyExistWrapException extends RuntimeException{
    public AlreadyExistWrapException(String wrapName){
        super("already wrap "+ wrapName);
    }
}
