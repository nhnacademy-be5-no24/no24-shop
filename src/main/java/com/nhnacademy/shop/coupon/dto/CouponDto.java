package com.nhnacademy.shop.coupon.dto.response;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class ResponseCouponDto {

    private Long couponId;

    private String couponName;

    private LocalDateTime deadline;

    private Long couponStatus;

    private Coupon.CouponType couponType;

    private Coupon.CouponTarget couponTarget;
}
