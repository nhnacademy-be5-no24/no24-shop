package com.nhnacademy.shop.orders.exception;

public class TooManyOrderException extends RuntimeException{
    public TooManyOrderException(String bookTitle){
        super(bookTitle + " 수량이 부족합니다.");
    }
}
