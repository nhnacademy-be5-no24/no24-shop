package com.nhnacademy.shop.grade.dto.response;

import com.nhnacademy.shop.grade.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등급 응답 dto
 *
 * @Author : 박병휘
 * @Date : 2024/05/03
 */
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GradeResponseDto {
    private Long gradeId;
    private String gradeName;
    private String gradeNameKor;
    private Long accumulateRate;
    private Integer tenPercentCoupon;
    private Integer twentyPercentCoupon;
    private Long requiredPayment;

    public GradeResponseDto(Grade grade) {
        this.gradeId = grade.getGradeId();
        this.gradeName = grade.getGradeName();
        this.gradeNameKor = grade.getGradeNameKor();
        this.accumulateRate = grade.getAccumulateRate();
        this.tenPercentCoupon = grade.getTenPercentCoupon();
        this.twentyPercentCoupon = grade.getTwentyPercentCoupon();
        this.requiredPayment = grade.getRequiredPayment();
    }
}
