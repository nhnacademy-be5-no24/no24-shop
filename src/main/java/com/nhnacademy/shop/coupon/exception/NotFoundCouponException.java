package com.nhnacademy.shop.coupon.exception;

/**
 * 쿠폰을 찾지 못 했을 때 발생하는 Exception 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException(Long couponId) {
        super(couponId + " does not exist.");
    }
}
