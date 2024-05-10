package com.nhnacademy.shop.coupon.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Coupon Entity Test
 *
 * @Author : 박병휘
 * @Date : 2024/04/18
 */
class CouponEntityTest {
    Coupon coupon1, coupon2, coupon3;

    Long couponId = 1L;
    String couponName = "Test Coupon";
    LocalDate deadline = LocalDate.now();
    Coupon.Status status = Coupon.Status.ACTIVE;
    Coupon.CouponType type = Coupon.CouponType.PERCENTAGE;
    Coupon.CouponTarget target = Coupon.CouponTarget.NORMAL;

    @BeforeEach
    public void setUp() {
        // setup coupon1
        coupon1 = Coupon.builder()
                .couponId(couponId)
                .couponName(couponName)
                .deadline(deadline)
                .couponStatus(status)
                .couponType(type)
                .couponTarget(target)
                .build();

        // setup coupon1
        couponId = 1L;
        couponName = "Test Coupon";
        status = Coupon.Status.ACTIVE;
        type = Coupon.CouponType.PERCENTAGE;
        target = Coupon.CouponTarget.NORMAL;

        coupon2 = Coupon.builder()
                .couponId(couponId)
                .couponName(couponName)
                .deadline(deadline)
                .couponStatus(status)
                .couponType(type)
                .couponTarget(target)
                .build();

        // setup coupon3
        couponId = 2L;
        couponName = "Test Coupon2";
        deadline = LocalDate.now();
        status = Coupon.Status.DEACTIVATED;
        type = Coupon.CouponType.AMOUNT;
        target = Coupon.CouponTarget.CATEGORY;

        coupon3 = Coupon.builder()
                .couponId(couponId)
                .couponName(couponName)
                .deadline(deadline)
                .couponStatus(status)
                .couponType(type)
                .couponTarget(target)
                .build();
    }

    @Test
    void testCouponNoArgsConstructor() {
        assertEquals(coupon1, coupon2);
        assertNotEquals(coupon1, coupon3);
    }

    @Test
    void testCouponEqualsAndHashCode() {
        Coupon coupon = new Coupon();

        assertNotNull(coupon);
    }

    @Test
    void testCouponGetter() {
        assertEquals(couponId, coupon3.getCouponId());
        assertEquals(couponName, coupon3.getCouponName());
        assertEquals(deadline, coupon3.getDeadline());
        assertEquals(status, coupon3.getCouponStatus());
        assertEquals(type, coupon3.getCouponType());
        assertEquals(target, coupon3.getCouponTarget());
    }
}