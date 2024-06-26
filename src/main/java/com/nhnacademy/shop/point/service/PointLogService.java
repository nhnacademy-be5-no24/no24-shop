package com.nhnacademy.shop.point.service;

import com.nhnacademy.shop.point.dto.request.PointRequestDto;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 포인트 내역 서비스 입니다.
 *
 * @author : 강병구, 박병휘
 * @date : 2024-04-04
 */

public interface PointLogService {
    /**
     * 관리자가 전체 포인트 내역 조회를 위한 메소드 입니다.
     *
     * @param pageable 페이징 정보
     * @return PointResponseDto 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    Page<PointResponseDto> getPoints(Pageable pageable);

    /**
     * 회원의 잔여 포인트 조회를 위한 메소드입니다.
     * @param customerNo
     * @return
     */
    Long getPoint(Long customerNo);

    /**
     * 회원 포인트 내역 조회를 위한 메소드 입니다.
     *
     * @param customerNo 조회를 위한 회원 번호
     * @param pageable 페이징 정보
     * @return PointResponseDto 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    Page<PointResponseDto> getPointsByCustomerNo(Long customerNo, Pageable pageable);

    /**
     * 회원 포인트 내역 날짜 별 조회를 위한 메소드 입니다.
     *
     * @param customerNo 조회를 위한 회원 번호
     * @param startDate  시작 일자
     * @param endDate    종료 일자
     * @param pageable 페이징 정보
     * @return PointResponseDto 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    Page<PointResponseDto> getPointsByCustomerNoAndCreatedAt(Long customerNo, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 회원 포인트 내역 생성을 위한 메소드 입니다.
     *
     * @param pointRequestDto 생성할 포인트 내역 정보를 담은 Dto
     * @return PointResponseDto 생성된 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    PointResponseDto createPointLog(PointRequestDto pointRequestDto);

}
