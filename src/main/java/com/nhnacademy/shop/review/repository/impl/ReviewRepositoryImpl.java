package com.nhnacademy.shop.review.repository.impl;

import com.nhnacademy.shop.book.entity.QBook;
import com.nhnacademy.shop.member.domain.QMember;
import com.nhnacademy.shop.review.domain.QReview;
import com.nhnacademy.shop.review.domain.Review;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.review.repository.ReviewRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 레포지토리 구현체 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-02
 */
public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom {
    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    QReview review = QReview.review;
    QBook book = QBook.book;
    QMember member = QMember.member;

    @Override
    public Page<ReviewResponseDto> findReviewsByBookIsbn(String bookIsbn, Pageable pageable) {
        JPQLQuery<Long> count = from(review)
                .select(review.count())
                .innerJoin(book)
                .on(review.book.bookIsbn.eq(book.bookIsbn))
                .where(review.book.bookIsbn.eq(bookIsbn));

        List<ReviewResponseDto> content = from(review)
                .innerJoin(book)
                .on(book.bookIsbn.eq(review.book.bookIsbn))
                .where(review.book.bookIsbn.eq(bookIsbn))
                .select(Projections.constructor(ReviewResponseDto.class,
                        review.reviewId,
                        review.reviewContent,
                        review.reviewScore,
                        review.reviewImage,
                        review.book.bookIsbn,
                        review.member.customerNo,
                        review.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public Page<ReviewResponseDto> findReviewsByCustomerNo(Long customerNo, Pageable pageable) {
        JPQLQuery<Long> count = from(review)
                .select(review.count())
                .innerJoin(member)
                .on(review.member.customerNo.eq(member.customerNo))
                .where(review.member.customerNo.eq(customerNo));

        List<ReviewResponseDto> content = from(review)
                .innerJoin(member)
                .on(review.member.customerNo.eq(member.customerNo))
                .where(review.member.customerNo.eq(customerNo))
                .select(Projections.constructor(ReviewResponseDto.class,
                        review.reviewId,
                        review.reviewContent,
                        review.reviewScore,
                        review.reviewImage,
                        review.book.bookIsbn,
                        review.member.customerNo,
                        review.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public Page<ReviewResponseDto> findReviews(Pageable pageable) {
        JPQLQuery<Long> count = from(review)
                .select(review.count());

        List<ReviewResponseDto> content = from(review)
                .select(Projections.constructor(ReviewResponseDto.class,
                        review.reviewId,
                        review.reviewContent,
                        review.reviewScore,
                        review.reviewImage,
                        review.book.bookIsbn,
                        review.member.customerNo,
                        review.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public Optional<ReviewResponseDto> findReview(Long reviewId) {
        ReviewResponseDto dto = from(review)
                .where(review.reviewId.eq(reviewId))
                .select(Projections.constructor(ReviewResponseDto.class,
                        review.reviewId,
                        review.reviewContent,
                        review.reviewScore,
                        review.reviewImage,
                        review.book.bookIsbn,
                        review.member.customerNo,
                        review.createdAt))
                .fetchOne();
        return Optional.of(dto);
    }
}
