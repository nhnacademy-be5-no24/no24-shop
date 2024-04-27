package com.nhnacademy.shop.coupon_member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
@Builder
@Getter
public class CouponMemberRequestDto {
    @NonNull
    private Long couponId;
    @NonNull
    private Long customerNo;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime destroyedAt;
    private LocalDateTime usedAt;
}
