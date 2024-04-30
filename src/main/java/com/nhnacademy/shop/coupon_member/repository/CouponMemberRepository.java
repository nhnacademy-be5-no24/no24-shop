package com.nhnacademy.shop.coupon_member.repository;

import com.nhnacademy.shop.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Coupon Member Repository.
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
public interface CouponMemberRepository extends JpaRepository<CouponMember, Long>, CouponMemberRepositoryCustom {
    Page<CouponMember> findCouponMembersByMember_CustomerNo(Long customerNo, Pageable pageable);
    List<CouponMember> findCouponMembersByMember_CustomerNo(Long customerNo);
}
