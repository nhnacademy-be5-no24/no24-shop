package com.nhnacademy.shop.grade.service;

import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.dto.request.GradeRequestDto;
import com.nhnacademy.shop.grade.dto.response.GradeResponseDto;
import com.nhnacademy.shop.grade.exception.AlreadyExistsGradeException;
import com.nhnacademy.shop.grade.exception.NotFoundGradeException;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.grade.service.impl.GradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 등급 Service 테스트
 *
 * @Author : 박병휘
 * @Date : 2024/05/10
 */
class GradeServiceTest {
    private GradeService gradeService;
    private Grade grade1, grade2, grade4, updateGrade;
    private GradeRepository gradeRepository;
    private GradeRequestDto gradeRequestDto, updateGradeRequestDto, illegalGradeRequestDto;

    @BeforeEach
    void setUp() {
        gradeRepository = mock(GradeRepository.class);
        gradeService = new GradeServiceImpl(gradeRepository);

        grade1 = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .gradeNameKor("일반")
                .accumulateRate(1L)
                .tenPercentCoupon(0)
                .twentyPercentCoupon(0)
                .requiredPayment(0L)
                .build();

        grade2 = Grade.builder()
                .gradeId(2L)
                .gradeName("ROYAL")
                .gradeNameKor("로얄")
                .accumulateRate(2L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(0)
                .requiredPayment(100000L)
                .build();

        grade4 = Grade.builder()
                .gradeId(4L)
                .gradeName("PLATINUM")
                .gradeNameKor("플래티넘")
                .accumulateRate(4L)
                .tenPercentCoupon(2)
                .twentyPercentCoupon(1)
                .requiredPayment(300000L)
                .build();

        updateGrade = Grade.builder()
                .gradeId(2L)
                .gradeName("ROYAL")
                .gradeNameKor("로얄")
                .accumulateRate(2L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(0)
                .requiredPayment(150000L)
                .build();

        updateGradeRequestDto = GradeRequestDto.builder()
                .gradeId(2L)
                .gradeName("ROYAL")
                .gradeNameKor("로얄")
                .accumulateRate(2L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(0)
                .requiredPayment(150000L)
                .build();

        gradeRequestDto = GradeRequestDto.builder()
                .gradeId(4L)
                .gradeName("PLATINUM")
                .gradeNameKor("플래티넘")
                .accumulateRate(4L)
                .tenPercentCoupon(2)
                .twentyPercentCoupon(1)
                .requiredPayment(300000L)
                .build();

        illegalGradeRequestDto = GradeRequestDto.builder()
                .gradeId(2L)
                .gradeName("ROYAL")
                .gradeNameKor("로얄")
                .accumulateRate(2L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(0)
                .requiredPayment(100000L)
                .build();

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade1));
        when(gradeRepository.findById(2L)).thenReturn(Optional.of(grade2));
        when(gradeRepository.findById(3L)).thenReturn(Optional.empty());
        when(gradeRepository.existsById(1L)).thenReturn(true);
        when(gradeRepository.existsById(2L)).thenReturn(true);
        when(gradeRepository.existsById(3L)).thenReturn(false);
        when(gradeRepository.findAll()).thenReturn(List.of(grade1, grade2));
    }

    @Test
    void findAllGrades() {
        List<GradeResponseDto> gradeResponseDtoList = gradeService.findAllGrades();

        assertNotNull(gradeResponseDtoList);
        assertEquals(gradeResponseDtoList.size(), 2);
    }

    @Test
    @DisplayName(value="존재하는 ID 등급 조회")
    void findGradeById() {
        GradeResponseDto gradeResponseDto = gradeService.findGradeById(1L);

        assertNotNull(gradeResponseDto);
        assertEquals(gradeResponseDto.getGradeId(), 1L);
        assertEquals(gradeResponseDto.getGradeName(), "NORMAL");
        assertEquals(gradeResponseDto.getAccumulateRate(), 1L);
        assertEquals(gradeResponseDto.getTenPercentCoupon(), 0);
        assertEquals(gradeResponseDto.getTwentyPercentCoupon(), 0);
        assertEquals(gradeResponseDto.getRequiredPayment(), 0L);
    }

    @Test
    @DisplayName(value="없는 ID 등급 조회")
    void findGradeByDoesNotExistId() {
        assertThrows(NotFoundGradeException.class, () -> gradeService.findGradeById(3L));
    }

    @Test
    @DisplayName(value="새로운 등급 등록")
    void saveGrade() {
        when(gradeRepository.save(any())).thenReturn(grade4);
        GradeResponseDto gradeResponseDto = gradeService.saveGrade(gradeRequestDto);

        assertNotNull(gradeResponseDto);
        assertEquals(gradeResponseDto.getGradeId(), 4L);
        assertEquals(gradeResponseDto.getGradeName(), "PLATINUM");
        assertEquals(gradeResponseDto.getAccumulateRate(), 4L);
        assertEquals(gradeResponseDto.getTenPercentCoupon(), 2);
        assertEquals(gradeResponseDto.getTwentyPercentCoupon(), 1);
        assertEquals(gradeResponseDto.getRequiredPayment(), 300000L);
    }

    @Test
    @DisplayName(value="중복 등급 아이디 등록")
    void saveIllegalGrade() {
        assertThrows(AlreadyExistsGradeException.class, () -> gradeService.saveGrade(illegalGradeRequestDto));
    }

    @Test
    @DisplayName(value="등급 업데이트")
    void updateGrade() {
        when(gradeRepository.save(any())).thenReturn(updateGrade);
        GradeResponseDto updateGradeResponseDto = gradeService.updateGrade(updateGradeRequestDto);

        assertNotNull(updateGradeResponseDto);
        assertEquals(updateGradeResponseDto.getGradeId(), 2L);
        assertEquals(updateGradeResponseDto.getGradeName(), "ROYAL");
        assertEquals(updateGradeResponseDto.getAccumulateRate(), 2L);
        assertEquals(updateGradeResponseDto.getTenPercentCoupon(), 1);
        assertEquals(updateGradeResponseDto.getTwentyPercentCoupon(), 0);
        assertEquals(updateGradeResponseDto.getRequiredPayment(), 150000L);
    }

    @Test
    @DisplayName(value="없는 등급 업데이트")
    void updateDoesNotExistGradeId() {
        illegalGradeRequestDto = GradeRequestDto.builder()
                .gradeId(3L)
                .build();

        assertThrows(NotFoundGradeException.class, () -> gradeService.updateGrade(illegalGradeRequestDto));

    }
}