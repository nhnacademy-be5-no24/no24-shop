package com.nhnacademy.shop.coupon.entity;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import javax.persistence.*;

/**
 * 할인율 쿠폰 테이블 입니다.
 *
 * @author : 박병휘
 * @date : 2024/03/29
 */
@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "percentage_coupon")
public class PercentageCoupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "discount_rate", nullable = false)
    private Long discountRate;

    @Column(name = "max_discount_price", nullable = false)
    private Long maxDiscountPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
