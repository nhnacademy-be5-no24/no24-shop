package com.nhnacademy.shop.coupon.exception;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException(Long couponId) {
        super(couponId + " does not exist.");
    }
}
