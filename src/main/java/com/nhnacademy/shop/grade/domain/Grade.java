package com.nhnacademy.shop.grade.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "grade_name")
    private String gradeName;

    @Column(name = "grade_name_kor")
    private String gradeNameKor;

    @Column(name = "accumulate_rate")
    private Long accumulateRate;

    @Column(name = "ten_percent_coupon")
    private Integer tenPercentCoupon;

    @Column(name = "twenty_percent_coupon")
    private Integer twentyPercentCoupon;

    @Column(name = "required_payment")
    private Long requiredPayment;
}