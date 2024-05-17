package com.nhnacademy.shop.coupon_member.service;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDtoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/26
 */
public interface CouponMemberService {
    public Long getCouponIdByCouponMemberId(Long couponMemberId);
    public CouponMemberResponseDto createCouponMember(Long couponId, Long customerNo);
    public CouponMemberResponseDtoList getCouponMemberByMember(Long customerNo, Pageable pageable);
    public CouponMemberResponseDto modifyCouponMemberStatus(Long couponMemberId, CouponMember.Status status);
}
