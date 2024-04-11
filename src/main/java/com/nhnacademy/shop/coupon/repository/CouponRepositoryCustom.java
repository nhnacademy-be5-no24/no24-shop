package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 쿠폰 QueryDSL 사용하기 위한 interface 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-09
 */
public interface CouponRepositoryCustom {

    /**
     * 쿠폰 목록 조회.
     *
     * @param pageable 페이지 정보
     * @return 쿠폰 페이지 반환.
     */
    Page<CouponResponseDto> findAllCoupons(Pageable pageable);

    /**
     * 도서 쿠폰 목록 조회.
     *
     * @param bookIsbn 도서 고유 번호
     * @param pageable 페이지 정보
     * @return 쿠폰 페이지 반환.
     */
    Page<CouponResponseDto> findBookCoupons(String bookIsbn, Pageable pageable);

    /**
     * 카테고리 쿠폰 목록 조회.
     *
     * @param categoryId 카테고리 아이디
     * @param pageable 페이지 정보
     * @return 쿠폰 페이지 반환.
     */
    Page<CouponResponseDto> findCategoryCoupons(Long categoryId, Pageable pageable);

    /**
     * 쿠폰 단건 조회.
     *
     * @param couponId 쿠폰 아이디
     * @return 쿠폰 정보를 담은 dto 반환.
     */
    Optional<CouponResponseDto> findCouponById(Long couponId);

    /**
     * 쿠폰 목록 조회.
     *
     * @param couponName 쿠폰 이름
     * @param pageable 페이지 정보
     * @return 쿠폰 페이지 반환.
     */
    Page<CouponResponseDto> findCouponsByContainingName(String couponName, Pageable pageable);
}
