package com.nhnacademy.shop.point.repository;

import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;

/**
 * 포인트 내역 QueryDSL을 사용하기 위한 interface 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */
@NoRepositoryBean
public interface PointLogRepositoryCustom {

    /**
     * 전체 포인트 내역 조회.
     *
     * @param pageable 페이징을 위한 페이지 정보
     * @return 전체 포인트 내역 페이지 반환
     */
    Page<PointResponseDto> findPoints(Pageable pageable);

    /**
     * 회원의 전체 포인트 내역 조회.
     *
     * @param customerNo 포인트 내역 조회를 위한 회원 번호
     * @param pageable   페이징을 위한 페이지 정보
     * @return 전체 포인트 내역 페이지 반환
     */
    Page<PointResponseDto> findPointsByCustomerNo(Long customerNo, Pageable pageable);

    /**
     * 회원의 날짜 별 포인트 내역 조회.
     *
     * @param customerNo 포인트 내역 조회를 위한 회원 번호
     * @param startDate  시작 일자
     * @param endDate    종료 일자
     * @param pageable   페이징을 위한 페이지 정보
     * @return 전체 포인트 내역 페이지 반환
     */
    Page<PointResponseDto> findPointsByCustomerNoAndCreatedAt(Long customerNo, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
