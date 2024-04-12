package com.nhnacademy.shop.coupon.service;

import com.nhnacademy.shop.coupon.dto.*;

import java.util.List;

/**
 * Coupon Service
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
public interface CouponService {
    List<CouponDto> getAllCoupons();
    List<CouponDto> getBookCoupons(String bookIsbn);
    List<CouponDto> getCategoryCoupons(Long categoryId);
    CouponDto getCouponById(Long couponId);
    List<CouponDto> getCouponsByContainingName(String couponName);
    CouponDto saveCoupon(CouponDto couponDto);

    void deleteCoupon(Long couponId);
}
