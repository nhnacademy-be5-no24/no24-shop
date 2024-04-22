package com.nhnacademy.shop.author.exception;

public class NotFoundAuthorException extends RuntimeException{
    public static final String Message = "저자가 없습니다.";
    public NotFoundAuthorException(){
        super(Message);
    }
}
