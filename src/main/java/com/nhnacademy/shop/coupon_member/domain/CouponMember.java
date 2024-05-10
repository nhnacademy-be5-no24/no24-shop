package com.nhnacademy.shop.coupon_member.domain;

import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Coupon Member 관계 테이블
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CouponMember {
    public enum Status {
        ACTIVE, USED, DESTROYED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_member_id")
    private Long couponMemberId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "customer_no")
    private Member member;

    @Column(name = "used")
    private boolean used;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "destroyed_at")
    private LocalDateTime destroyedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "status")
    private Status status;

    public CouponMember setDestroyedAtToNow() {
        this.destroyedAt = LocalDateTime.now();
        return this;
    }

    public CouponMember setUsedAtToNow() {
        this.usedAt = LocalDateTime.now();
        this.used = true;
        return this;
    }

    public CouponMember setStatus(Status status) {
        this.status = status;
        return this;
    }

    public CouponMember setCouponActivate() {
        this.status = Status.ACTIVE;
        this.used = false;
        this.usedAt = null;
        return this;
    }
}
