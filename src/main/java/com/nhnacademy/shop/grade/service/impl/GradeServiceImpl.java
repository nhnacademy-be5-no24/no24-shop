package com.nhnacademy.shop.grade.service.impl;

import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.dto.request.GradeRequestDto;
import com.nhnacademy.shop.grade.dto.response.GradeResponseDto;
import com.nhnacademy.shop.grade.exception.AlreadyExistsGradeException;
import com.nhnacademy.shop.grade.exception.NotFoundGradeException;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.grade.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/05/03
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;


    @Override
    public List<GradeResponseDto> findAllGrades() {
        return gradeRepository.findAll().stream()
                .map(grade -> GradeResponseDto.builder()
                        .gradeId(grade.getGradeId())
                        .gradeName(grade.getGradeName())
                        .gradeNameKor(grade.getGradeNameKor())
                        .accumulateRate(grade.getAccumulateRate())
                        .tenPercentCoupon(grade.getTenPercentCoupon())
                        .twentyPercentCoupon(grade.getTwentyPercentCoupon())
                        .requiredPayment(grade.getRequiredPayment())
                        .build()
                )
                .sorted((a, b) -> (int) (a.getGradeId() - b.getGradeId()))
                .collect(Collectors.toList());
    }

    @Override
    public GradeResponseDto findGradeById(Long gradeId) {
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);

        if(optionalGrade.isEmpty()) {
            throw new NotFoundGradeException();
        }

        return GradeResponseDto.builder()
                .gradeId(gradeId)
                .gradeName(optionalGrade.get().getGradeName())
                .gradeNameKor(optionalGrade.get().getGradeNameKor())
                .accumulateRate(optionalGrade.get().getAccumulateRate())
                .tenPercentCoupon(optionalGrade.get().getTenPercentCoupon())
                .twentyPercentCoupon(optionalGrade.get().getTwentyPercentCoupon())
                .requiredPayment(optionalGrade.get().getRequiredPayment())
                .build();
    }

    @Override
    public GradeResponseDto saveGrade(GradeRequestDto gradeRequestDto) {
        if(gradeRepository.existsById(gradeRequestDto.getGradeId())) {
            throw new AlreadyExistsGradeException();
        }

        Grade grade = Grade.builder()
                .gradeId(null)
                .gradeName(gradeRequestDto.getGradeName())
                .gradeNameKor(gradeRequestDto.getGradeNameKor())
                .accumulateRate(gradeRequestDto.getAccumulateRate())
                .tenPercentCoupon(gradeRequestDto.getTenPercentCoupon())
                .twentyPercentCoupon(gradeRequestDto.getTwentyPercentCoupon())
                .requiredPayment(gradeRequestDto.getRequiredPayment())
                .build();

        return new GradeResponseDto(gradeRepository.save(grade));
    }

    @Override
    public GradeResponseDto updateGrade(GradeRequestDto gradeRequestDto) {
        if(!gradeRepository.existsById(gradeRequestDto.getGradeId())) {
            throw new NotFoundGradeException();
        }

        Grade grade = Grade.builder()
                .gradeId(gradeRequestDto.getGradeId())
                .gradeName(gradeRequestDto.getGradeName())
                .gradeNameKor(gradeRequestDto.getGradeNameKor())
                .accumulateRate(gradeRequestDto.getAccumulateRate())
                .tenPercentCoupon(gradeRequestDto.getTenPercentCoupon())
                .twentyPercentCoupon(gradeRequestDto.getTwentyPercentCoupon())
                .requiredPayment(gradeRequestDto.getRequiredPayment())
                .build();

        return new GradeResponseDto(gradeRepository.save(grade));
    }
}
