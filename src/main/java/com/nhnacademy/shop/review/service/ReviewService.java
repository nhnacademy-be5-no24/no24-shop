package com.nhnacademy.shop.review.service;

import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * 리뷰 서비스입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 */
@Service
public interface ReviewService {
    /**
     * 특정 상품에 대한 리뷰 리스트 조회를 위한 메소드 입니다.
     *
     * @param bookIsbn 도서 고유 번호
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return 리뷰 정보를 담은 Dto
     */
    Page<ReviewResponseDto> getReviewsByBookIsbn(String bookIsbn, Integer pageSize, Integer offset);

    /**
     * 특정 회원이 작성한 리뷰 리스트 조회를 위한 메소드 입니다.
     *
     * @param customerNo 회원 번호
     * @param pageSize   페이지 사이즈
     * @param offset     페이지 오프셋
     * @return 리뷰 정보를 담은 Dto
     */
    Page<ReviewResponseDto> getReviewsByCustomerNo(Long customerNo, Integer pageSize, Integer offset);

    /**
     * 리뷰 전체 조회를 위한 메소드 입니다.
     *
     * @param pageSize 페이지 사이즈
     * @param offset   페이지 오프셋
     * @return 리뷰 정보를 담은 Dto 리스트
     */
    Page<ReviewResponseDto> getReviews(Integer pageSize, Integer offset);

    /**
     * 리뷰 단건 조회를 위한 메소드 입니다.
     *
     * @param reviewId 리뷰 아이디
     * @return 리뷰 정보를 담은 Dto
     */
    ReviewResponseDto getReviewByReviewId(Long reviewId);

    /**
     * 리뷰 등록을 위한 메소드 입니다.
     *
     * @param createReviewRequestDto 등록할 리뷰 정보를 담은 Dto
     * @return 리뷰 정보를 담은 Dto
     */
    ReviewResponseDto createReview(CreateReviewRequestDto createReviewRequestDto);

    /**
     * 리뷰 수정을 위한 메소드 입니다.
     *
     * @param modifyReviewRequestDto 수정할 리뷰 정보를 담은 Dto
     * @return 리뷰 정보를 담은 Dto
     */
    ReviewResponseDto modifyReview(ModifyReviewRequestDto modifyReviewRequestDto);

    /**
     * 리뷰 삭제를 위한 메소드 입니다.
     *
     * @param reviewId 리뷰 아이디
     */
    void deleteReview(Long reviewId);

}
