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
@Table(name = "amount_coupon")
public class AmountCoupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "discount_price", nullable = false)
    private Long discountPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
