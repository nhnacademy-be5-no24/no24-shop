package com.nhnacademy.shop.coupon.entity;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import javax.persistence.*;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_coupon")
public class CategoryCoupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
