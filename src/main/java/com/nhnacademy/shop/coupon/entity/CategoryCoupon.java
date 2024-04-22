package com.nhnacademy.shop.coupon.entity;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import javax.persistence.*;

/**
 * 카테고리 전용 쿠폰 테이블 입니다.
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
@Table(name = "category_coupon")
public class CategoryCoupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
