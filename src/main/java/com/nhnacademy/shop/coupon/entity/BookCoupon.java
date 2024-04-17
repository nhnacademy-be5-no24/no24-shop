package com.nhnacademy.shop.coupon.entity;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.*;

import javax.persistence.*;

/**
 * 도서 전용 쿠폰 테이블 입니다.
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
@Table(name = "book_coupon")
public class BookCoupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "book_isbn", nullable = false)
    private String bookIsbn;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
