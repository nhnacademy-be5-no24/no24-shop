package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.coupon.entity.BookCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 도서 전용 쿠폰 레포지토리 테이블 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
public interface BookCouponRepository extends JpaRepository<BookCoupon, Long> {
}
