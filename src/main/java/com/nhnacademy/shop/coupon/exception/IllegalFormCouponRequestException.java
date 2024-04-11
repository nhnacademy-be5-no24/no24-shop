package com.nhnacademy.shop.coupon.exception;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
public class IllegalFormCouponRequestException extends RuntimeException {
    public IllegalFormCouponRequestException() {
        super("Form does not legal.");
    }
}
