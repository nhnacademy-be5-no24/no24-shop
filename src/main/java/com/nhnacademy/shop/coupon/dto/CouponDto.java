package com.nhnacademy.shop.coupon.dto;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {

    private Long couponId;
    private String couponName;
    private Date deadline;
    private Coupon.Status couponStatus;
    private Coupon.CouponType couponType;
    private Coupon.CouponTarget couponTarget;

    // for target
    private String bookIsbn;
    private Long categoryId;

    // for coupon type
    private Long discountPrice;
    private Long discountRate;
    private Long maxDiscountPrice;
}
