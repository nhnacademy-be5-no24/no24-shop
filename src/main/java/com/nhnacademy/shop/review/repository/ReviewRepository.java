package com.nhnacademy.shop.review.repository;

import com.nhnacademy.shop.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 리뷰(Review) 테이블 레포지토리 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-01
 **/

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

}
