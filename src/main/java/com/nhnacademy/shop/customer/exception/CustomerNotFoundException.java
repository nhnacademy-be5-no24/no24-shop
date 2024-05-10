package com.nhnacademy.shop.customer.exception;

/**
 * Customer를 못찾았을 때 반환하는 exception
 *
 * @Author : 박병휘
 * @Date : 2024/04/25
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("해당 사용자를 찾을 수 없습니다.");
    }
}
