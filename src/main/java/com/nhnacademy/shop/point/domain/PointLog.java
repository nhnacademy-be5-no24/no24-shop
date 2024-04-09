package com.nhnacademy.shop.point.domain;

import com.nhnacademy.shop.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 포인트 내역 (PointLog) 테이블 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "point_log")
public class PointLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    private Long pointId;

    @ManyToOne
    @JoinColumn(name = "customer_no", nullable = false)
    private Member member;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "point_description", nullable = false)
    private String pointDescription;

    @Column(name = "usage", nullable = false)
    private Integer usage;

    @Column(name = "type", nullable = false)
    private Boolean type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
