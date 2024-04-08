package com.nhnacademy.shop.point.service;

import com.nhnacademy.shop.point.dto.request.PointRequestDto;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 포인트 내역 서비스 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */

public interface PointLogService {
    /**
     * 관리자가 전체 포인트 내역 조회를 위한 메소드 입니다.
     *
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return PointResponseDto 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    Page<PointResponseDto> getPointLogs(Integer pageSize, Integer offset);

    /**
     * 회원 포인트 내역 조회를 위한 메소드 입니다.
     *
     * @param customerNo 조회를 위한 회원 번호
     * @param pageSize   페이지 사이즈
     * @param offset     페이지 오프셋
     * @return PointResponseDto 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    Page<PointResponseDto> getPointsByCustomerNo(Long customerNo, Integer pageSize, Integer offset);

    /**
     * 회원 포인트 내역 날짜 별 조회를 위한 메소드 입니다.
     *
     * @param customerNo 조회를 위한 회원 번호
     * @param startDate  시작 일자
     * @param endDate    종료 일자
     * @param pageSize   페이지 사이즈
     * @param offset     페이지 오프셋
     * @return PointResponseDto 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    Page<PointResponseDto> getPointsByCustomerNoAndCreatedAt(Long customerNo, LocalDateTime startDate, LocalDateTime endDate, Integer pageSize, Integer offset);

    /**
     * 회원 포인트 내역 생성을 위한 메소드 입니다.
     *
     * @param pointRequestDto 생성할 포인트 내역 정보를 담은 Dto
     * @return PointResponseDto 생성된 포인트 내역 정보를 담고 있는 Dto 페이지가 반환됩니다.
     */
    PointResponseDto createPointLog(PointRequestDto pointRequestDto);

}
