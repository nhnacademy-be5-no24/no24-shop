package com.nhnacademy.shop.review.repository;

import com.nhnacademy.shop.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}