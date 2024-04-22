package com.nhnacademy.shop.coupon.dto.response;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import java.util.Date;

/**
 * 쿠폰 기본 정보를 반환하기 위한 dto 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseDto {

    private Long couponId;
    private String couponName;
    private Date deadline;
    private int issueLimit;
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
