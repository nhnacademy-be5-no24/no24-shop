package com.nhnacademy.shop.coupon_member.exception;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/26
 */
public class MemberDoesNotHaveCouponException extends RuntimeException {
    public MemberDoesNotHaveCouponException(String message) {
        super(message);
    }
}
