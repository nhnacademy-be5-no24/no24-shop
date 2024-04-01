package com.nhnacademy.shop.review.service;

import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 리뷰 서비스입니다.
 *
 * @author : 강병구
 * @since : 1.0
 */
@Service
public interface ReviewService {
    /**
     * 특정 상품에 대한 상품평 리스트 조회를 위한 메소드 입니다.
     *
     * @param bookIsbn 도서 고유 번호
     * @return 상품평 정보를 담은 Dto
     */
    List<ReviewResponseDto> getReviewByBookIsbn(String bookIsbn);

    /**
     * 특정 회원이 작성한 상품평 리스트 조회를 위한 메소드 입니다.
     *
     * @param customerNo 회원 번호
     * @return 상품평 정보를 담은 Dto
     */
    List<ReviewResponseDto> getReviewByCustomerNo(Long customerNo);

    /**
     * 상품평 전체 조회를 위한 메소드 입니다.
     *
     * @return 상품평 정보를 담은 Dto 리스트
     */
    List<ReviewResponseDto> getReviews();

    /**
     * 상품평 단건 조회를 위한 메소드 입니다.
     *
     * @param reviewId 상품평 아이디
     * @return 상품평 정보를 담은 Dto
     */
    ReviewResponseDto getReviewByReviewId(Long reviewId);

    /**
     * 상품평 등록을 위한 메소드 입니다.
     *
     * @param createReviewRequestDto 등록할 상품평 정보를 담은 Dto
     * @return 상품평 정보를 담은 Dto
     */
    ReviewResponseDto createReview(CreateReviewRequestDto createReviewRequestDto);

    /**
     * 상품평 수정을 위한 메소드 입니다.
     *
     * @param modifyReviewRequestDto 수정할 상품평 정보를 담은 Dto
     * @return 상품평 정보를 담은 Dto
     */
    ReviewResponseDto modifyReview(ModifyReviewRequestDto modifyReviewRequestDto);

    /**
     * 상품평 삭제를 위한 메소드 입니다.
     *
     * @param reviewId 상품평 아이디
     *
     */
    void deleteReview(Long reviewId);

}
