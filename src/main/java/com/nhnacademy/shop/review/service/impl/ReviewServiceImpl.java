package com.nhnacademy.shop.review.service.impl;

import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.review.repository.ReviewRepository;
import com.nhnacademy.shop.review.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 상품평 서비스 구현체입니다.
 *
 * @author : 강병구
 * @since : 1.0
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * {@inheritDoc}
     * @throws : BookNotFoundException 도서가 존재하지 않을 때 발생하는 exception
     */
    @Override
    public List<ReviewResponseDto> getReviewByBookIsbn(String bookIsbn) {
        if(!bookRepository.existsById(bookIsbn)) {
            throw new BookNotFoundException();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReviewResponseDto> getReviewByCustomerNo(Long customerNo) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReviewResponseDto> getReviews() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewResponseDto getReviewByReviewId(Long reviewId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewResponseDto createReview(CreateReviewRequestDto createReviewRequestDto) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewResponseDto modifyReview(ModifyReviewRequestDto modifyReviewRequestDto) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReview(Long reviewId) {

    }
}
