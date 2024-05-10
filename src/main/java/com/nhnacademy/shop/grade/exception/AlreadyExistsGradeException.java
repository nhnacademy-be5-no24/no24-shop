package com.nhnacademy.shop.grade.exception;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/05/03
 */
public class AlreadyExistsGradeException extends RuntimeException{
    public AlreadyExistsGradeException() {
        super("Grade already exists");
    }
}
