package com.nhnacademy.shop.grade.service;

import com.nhnacademy.shop.grade.dto.request.GradeRequestDto;
import com.nhnacademy.shop.grade.dto.response.GradeResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * 등급 서비스
 *
 * @Author : 박병휘
 * @Date : 2024/05/03
 */
public interface GradeService {
    List<GradeResponseDto> findAllGrades();
    GradeResponseDto findGradeById(Long gradeId);
    GradeResponseDto saveGrade(GradeRequestDto gradeRequestDto);
    GradeResponseDto updateGrade(GradeRequestDto gradeRequestDto);
}
