package com.nhnacademy.shop.point.service;

import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.dto.request.PointRequestDto;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import com.nhnacademy.shop.point.repository.PointLogRepository;
import com.nhnacademy.shop.point.service.impl.PointLogServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Point Service 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-05
 */
class PointServiceTest {
    private PointLogRepository pointLogRepository;
    private MemberRepository memberRepository;
    private PointLogService pointLogService;
    PointLog pointLog;
    Member member;
    Pageable pageable;
    Integer pageSize;
    Integer offset;
    PointResponseDto pointResponseDto;
    PointRequestDto pointRequestDto;
    Page<PointResponseDto> pointPage;
    LocalDateTime startDate;
    LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        pointLogRepository = mock(PointLogRepository.class);
        memberRepository = mock(MemberRepository.class);
        pointLogService = new PointLogServiceImpl(pointLogRepository, memberRepository);
        member = new Member(1L, "abc111", null, 1L);
        pointLog = new PointLog(1L, member, 1L, "리뷰 작성", 500, false,
                LocalDateTime.parse("2024-04-05T00:00:00"));
        pageSize = 0;
        offset = 10;
        pageable = PageRequest.of(pageSize, offset);
        pointResponseDto = new PointResponseDto(1L, member.getCustomerNo(), 1L, "리뷰 작성", 500, false,
                LocalDateTime.parse("2024-04-05T00:00:00"));
        pointRequestDto = new PointRequestDto(member.getCustomerNo(), 1L, "리뷰 작성", 500, false,
                LocalDateTime.parse("2024-04-05T00:00:00"));
        pointPage = new PageImpl<>(List.of(pointResponseDto), pageable, 1);
        startDate = LocalDateTime.parse("2024-04-03T00:00:00");
        endDate = LocalDateTime.parse("2024-04-06T00:00:00");
    }

    @Test
    @Order(1)
    @DisplayName(value = "전체 포인트 내역 조회 성공")
    void getPointLogsTest() {
        when(pointLogRepository.save(any())).thenReturn(pointLog);
        when(pointLogRepository.findPoints(any())).thenReturn(pointPage);

        Page<PointResponseDto> dtoPage = pointLogService.getPoints(pageable);
        List<PointResponseDto> pointList = dtoPage.getContent();

        verify(pointLogRepository, times(1)).findPoints(any());

        assertThat(pointList).isNotEmpty();
        assertThat(pointList.get(0).getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(pointList.get(0).getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(pointList.get(0).getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(pointList.get(0).getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(pointList.get(0).getUsage()).isEqualTo(pointLog.getUsage());
        assertThat(pointList.get(0).getIsUsed()).isEqualTo(pointLog.getIsUsed());
        assertThat(pointList.get(0).getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName(value = "회원 포인트 내역 조회 성공")
    void getPointsByCustomerNoTest_Success() {
        when(pointLogRepository.save(any())).thenReturn(pointLog);
        when(memberRepository.existsById(anyLong())).thenReturn(true);
        when(pointLogRepository.findPointsByCustomerNo(anyLong(), any())).thenReturn(pointPage);

        Page<PointResponseDto> dtoPage = pointLogService.getPointsByCustomerNo(pointLog.getMember().getCustomerNo(), pageable);
        List<PointResponseDto> pointList = dtoPage.getContent();

        verify(memberRepository, times(1)).existsById(anyLong());
        verify(pointLogRepository, times(1)).findPointsByCustomerNo(anyLong(), any());

        assertThat(pointList).isNotEmpty();
        assertThat(pointList.get(0).getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(pointList.get(0).getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(pointList.get(0).getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(pointList.get(0).getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(pointList.get(0).getUsage()).isEqualTo(pointLog.getUsage());
        assertThat(pointList.get(0).getIsUsed()).isEqualTo(pointLog.getIsUsed());
        assertThat(pointList.get(0).getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }

    @Test
    @Order(3)
    @DisplayName(value = "회원 포인트 내역 조회 실패 - MemberNotFound")
    void getPointsByCustomerNoTest_MemberNotFound() {
        when(memberRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> pointLogService.getPointsByCustomerNo(anyLong(), pageable))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @Test
    @Order(4)
    @DisplayName(value = "회원 날짜 별 포인트 내역 조회 성공")
    void getPointsByCustomerNoAndCreatedAtTest_Success() {
        when(pointLogRepository.save(any())).thenReturn(pointLog);
        when(memberRepository.existsById(anyLong())).thenReturn(true);
        when(pointLogRepository.findPointsByCustomerNoAndCreatedAt(anyLong(), any(), any(), any())).thenReturn(pointPage);

        Page<PointResponseDto> dtoPage = pointLogService.getPointsByCustomerNoAndCreatedAt(pointLog.getMember().getCustomerNo(), startDate, endDate, pageable);
        List<PointResponseDto> pointList = dtoPage.getContent();

        verify(memberRepository, times(1)).existsById(anyLong());
        verify(pointLogRepository, times(1)).findPointsByCustomerNoAndCreatedAt(anyLong(), any(), any(), any());

        assertThat(pointList).isNotEmpty();
        assertThat(pointList.get(0).getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(pointList.get(0).getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(pointList.get(0).getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(pointList.get(0).getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(pointList.get(0).getUsage()).isEqualTo(pointLog.getUsage());
        assertThat(pointList.get(0).getIsUsed()).isEqualTo(pointLog.getIsUsed());
        assertThat(pointList.get(0).getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }

    @Test
    @Order(5)
    @DisplayName(value = "회원 날짜 별 포인트 내역 조회 실패 - MemberNotFound")
    void getPointsByCustomerNoAndCreatedAtTest_MemberNotFound() {
        when(memberRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> pointLogService.getPointsByCustomerNoAndCreatedAt(anyLong(), startDate, endDate, pageable))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @Test
    @Order(6)
    @DisplayName(value = "포인트 내역 생성 성공")
    void createPointLog_Success() {
        when(pointLogRepository.save(any())).thenReturn(pointLog);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        PointResponseDto dto = pointLogService.createPointLog(pointRequestDto);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(pointLogRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(dto.getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(dto.getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(dto.getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(dto.getUsage()).isEqualTo(pointLog.getUsage());
        assertThat(dto.getIsUsed()).isEqualTo(pointLog.getIsUsed());
        assertThat(dto.getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }

    @Test
    @Order(6)
    @DisplayName(value = "포인트 내역 생성 실패 - MemberNotFound")
    void createPointLog_MemberNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pointLogService.createPointLog(pointRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }
}
