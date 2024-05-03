package com.nhnacademy.shop.grade.exception;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/05/03
 */
public class NotFoundGradeException extends RuntimeException{
    public NotFoundGradeException() {
        super("Not Found Grade");
    }
}
