package com.nhnacademy.shop.coupon_member.dto.response;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * CouponMember의 응답 Dto
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
@Builder
@Getter
public class CouponMemberResponseDto {
    private Long couponMemberId;
    @NonNull
    private Long couponId;
    @NonNull
    private Long customerNo;
    private String couponName;
    @NonNull
    private LocalDateTime createdAt;
    private LocalDateTime destroyedAt;
    private LocalDateTime usedAt;
    private boolean used;

    private CouponMember.Status couponStatus;
    private Coupon.CouponType couponType;
    private Coupon.CouponTarget couponTarget;

    // for target
    private String bookIsbn;
    private Long categoryId;

    // for coupon type
    private Long discountPrice;
    private Long discountRate;
    private Long maxDiscountPrice;

    public static CouponMemberResponseDto buildDto(CouponMember couponMember, CouponResponseDto couponResponseDto) {
        return CouponMemberResponseDto.builder()
                .couponMemberId(couponMember.getCouponMemberId())
                .couponId(couponResponseDto.getCouponId())
                .couponName(couponResponseDto.getCouponName())
                .customerNo(couponMember.getMember().getCustomerNo())
                .createdAt(couponMember.getCreatedAt())
                .destroyedAt(couponMember.getDestroyedAt())
                .usedAt(couponMember.getUsedAt())
                .used(couponMember.isUsed())
                .couponStatus(couponMember.getStatus())
                .couponType(couponResponseDto.getCouponType())
                .couponTarget(couponResponseDto.getCouponTarget())
                .bookIsbn(couponResponseDto.getBookIsbn())
                .categoryId(couponResponseDto.getCategoryId())
                .discountPrice(couponResponseDto.getDiscountPrice())
                .discountRate(couponResponseDto.getDiscountRate())
                .maxDiscountPrice(couponResponseDto.getMaxDiscountPrice())
                .build();
    }
}
