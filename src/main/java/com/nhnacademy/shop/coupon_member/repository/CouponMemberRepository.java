package com.nhnacademy.shop.coupon_member.repository;

import com.nhnacademy.shop.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Coupon Member Repository.
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
public interface CouponMemberRepository extends JpaRepository<CouponMember, Long>, CouponMemberRepositoryCustom {
}
