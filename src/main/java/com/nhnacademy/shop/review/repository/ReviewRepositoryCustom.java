package com.nhnacademy.shop.review.repository;

import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * 리뷰 QueryDSL을 사용하기 위한 interface 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-02
 */
@NoRepositoryBean
public interface ReviewRepositoryCustom {

    /**
     * 도서에 작성된 리뷰들을 조회해오기 위한 메소드 입니다.
     *
     * @param bookIsbn 도서 고유 번호
     * @param pageable 페이지 정보
     * @return 리뷰 정보가 담긴 Dto Page 정보를 반환합니다.
     */
    Page<ReviewResponseDto> findReviewsByBookIsbn(String bookIsbn, Pageable pageable);

    /**
     * 회원이 작성한 리뷰들을 조회해오기 위한 메소드 입니다.
     *
     * @param customerNo 회원 번호
     * @param pageable   페이지 정보
     * @return 리뷰 정보가 담긴 Dto Page 정보를 반환합니다.
     */
    Page<ReviewResponseDto> findReviewsByCustomerNo(Long customerNo, Pageable pageable);

    /**
     * 전체 리뷰들을 조회해오기 위한 메소드 입니다.
     *
     * @param pageable 페이지 정보
     * @return 리뷰 정보가 담긴 Dto Page 정보를 반환합니다.
     */
    Page<ReviewResponseDto> findReviews(Pageable pageable);

    /**
     * 리뷰 단건을 조회해오기 위한 메소드 입니다.
     *
     * @param reviewId 리뷰 아이디
     * @return 리뷰 정보가 담긴 Dto 정보를 반환합니다.
     */
    Optional<ReviewResponseDto> findReview(Long reviewId);
}
