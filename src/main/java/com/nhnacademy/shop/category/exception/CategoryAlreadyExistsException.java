package com.nhnacademy.shop.category.exception;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException() {
        super("이미 존재하는 카테고리입니다.");
    }
}
