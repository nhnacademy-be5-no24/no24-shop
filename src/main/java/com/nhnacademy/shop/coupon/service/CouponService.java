package com.nhnacademy.shop.coupon.service;

import com.nhnacademy.shop.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Coupon Service
 *
 * @author : 박병휘, 강병구
 * @date : 2024/03/29
 */
public interface CouponService {

    /**
     * 쿠폰 전체 조회를 위한 메소드 입니다.
     *
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return 쿠폰 정보를 담은 Dto 리스트
     */
    Page<CouponResponseDto> getAllCoupons(Integer pageSize, Integer offset);

    /**
     * 도서 쿠폰 전체 조회를 위한 메소드 입니다.
     *
     * @param bookIsbn 도서 고유 번호
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return 쿠폰 정보를 담은 Dto 리스트
     */
    Page<CouponResponseDto> getBookCoupons(String bookIsbn, Integer pageSize, Integer offset);

    /**
     * 카테고리 쿠폰 전체 조회를 위한 메소드 입니다.
     *
     * @param categoryId 카테고리 아이디
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return 쿠폰 정보를 담은 Dto 리스트
     */
    Page<CouponResponseDto> getCategoryCoupons(Long categoryId, Integer pageSize, Integer offset);

    /**
     * 쿠폰 단건 조회를 위한 메소드 입니다.
     *
     * @param couponId 쿠폰 아이디
     * @return 쿠폰 정보를 담은 Dto
     */
    CouponResponseDto getCouponById(Long couponId);

    /**
     * 쿠폰 이름을 통한 쿠폰 목록 조회를 위한 메소드 입니다.
     *
     * @param couponName 쿠폰 이름
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return 쿠폰 정보를 담은 Dto 리스트
     */
    Page<CouponResponseDto> getCouponsByContainingName(String couponName, Integer pageSize, Integer offset);

    /**
     * 쿠폰 등록을 위한 메소드 입니다.
     *
     * @param couponDto 등록할 쿠폰 정보를 담은 Dto
     * @return 쿠폰 정보를 담은 Dto 리스트
     */
    CouponResponseDto saveCoupon(CouponRequestDto couponDto);

    /**
     * 쿠폰 삭제를 위한 메소드 입니다.
     *
     * @param couponId 쿠폰 아이디
     */
    void deleteCoupon(Long couponId);
}
