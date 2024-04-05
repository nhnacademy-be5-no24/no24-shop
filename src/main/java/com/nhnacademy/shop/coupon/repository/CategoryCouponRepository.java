package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.coupon.entity.CategoryCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon, Long> {
}
