package com.nhnacademy.shop.coupon_member.dto.request;

import com.nhnacademy.shop.coupon_member.domain.CouponMember;
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
public class CouponMemberStatusRequestDto {
    public enum Status {
        ACTIVE, USED, DESTROYED
    }
    private CouponMember.Status status;
}
