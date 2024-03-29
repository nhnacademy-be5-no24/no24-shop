package com.nhnacademy.shop.book.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException() {
        super("해당 도서를 찾을 수 없습니다.");
    }
}
