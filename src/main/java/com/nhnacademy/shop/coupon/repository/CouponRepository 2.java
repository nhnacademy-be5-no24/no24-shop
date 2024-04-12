package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("select c from Coupon c where c.couponName like %:couponName%")
    List<Coupon> findCouponsByContainingCouponName(@Param("couponName") String couponName);
}
