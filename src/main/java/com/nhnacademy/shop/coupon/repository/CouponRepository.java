package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 쿠폰(Coupon) 테이블 레포지토리 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {
}
