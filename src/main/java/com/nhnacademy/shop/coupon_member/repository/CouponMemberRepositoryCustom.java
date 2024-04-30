package com.nhnacademy.shop.coupon_member.repository;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Coupon Member 연관 테이블에서 QueryDSL을 사용하기 위한 interface.
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
@NoRepositoryBean
public interface CouponMemberRepositoryCustom {
    /**
     * 사용자 쿠폰 조회.
     * @param customerNo
     * @param pageable
     * @return
     */
    Page<CouponResponseDto> findByMemberCustomerNo(Long customerNo, Pageable pageable);

    /**
     * 사용하지 않은 사용자 쿠폰 조회.
     * @param customerNo
     * @param pageable
     * @return
     */
    Page<CouponResponseDto> findUnusedCouponByMemberCustomerNo(Long customerNo, Pageable pageable);

    /**
     * 사용하지 않은 사용자 책 쿠폰 조회.
     * @param customerNo
     * @param bookIsbn
     * @param pageable
     * @return
     */
    Page<CouponResponseDto> findUnusedBookCouponByMemberCustomerNo(Long customerNo, String bookIsbn, Pageable pageable);

    /**
     * 사용하지 않은 사용자 카테고리 쿠폰 조회.
     * @param customerNo
     * @param categoryId
     * @param pageable
     * @return
     */
    Page<CouponResponseDto> findUnusedCategoryCouponByMemberCustomerNoAndCategoryId(Long customerNo, Long categoryId, Pageable pageable);
}
