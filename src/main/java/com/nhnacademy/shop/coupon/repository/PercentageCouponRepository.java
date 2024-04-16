package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.coupon.entity.PercentageCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 할인율 쿠폰 테이블 레포지토리 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
public interface PercentageCouponRepository extends JpaRepository<PercentageCoupon, Long> {
}
