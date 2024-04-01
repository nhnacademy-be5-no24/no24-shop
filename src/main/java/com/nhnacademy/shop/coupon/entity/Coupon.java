package com.nhnacademy.shop.coupon.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
@Entity
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coupon")
public class Coupon {
    public enum CouponTarget {
        NORMAL, CATEGORY, BOOK
    }
    public enum CouponType {
        PERCENTAGE, AMOUNT
    }

    public enum Status {
        ACTIVE, DEACTIVATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "coupon_name", nullable = false)
    private String couponName;

    @Column(name = "deadline", nullable = false)
    private Date deadline;

    @Column(name = "coupon_status", nullable = false)
    private Status couponStatus;

    @Column(name = "coupon_type", nullable = false)
    private CouponType couponType;

    @Column(name = "coupon_target", nullable = false)
    private CouponTarget couponTarget;
}
