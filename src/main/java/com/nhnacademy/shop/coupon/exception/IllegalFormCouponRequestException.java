package com.nhnacademy.shop.coupon.exception;

/**
 * 쿠폰 등록 요청이 쿠폰 테이블 폼에 맞지 않을 때 발생하는 Exception 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
public class IllegalFormCouponRequestException extends RuntimeException {
    public IllegalFormCouponRequestException() {
        super("Form does not legal.");
    }
}
