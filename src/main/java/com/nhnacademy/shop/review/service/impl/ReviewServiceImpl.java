package com.nhnacademy.shop.review.service.impl;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.review.domain.Review;
import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.review.exception.ReviewNotFoundException;
import com.nhnacademy.shop.review.repository.ReviewRepository;
import com.nhnacademy.shop.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 리뷰 서비스 구현체입니다.
 *
 * @author : 강병구
 * @date : 2024-04-01
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
     *
     * @throws BookNotFoundException 도서가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviewsByBookIsbn(String bookIsbn, Integer pageSize, Integer offset) {
        if (!bookRepository.existsById(bookIsbn)) {
            throw new BookNotFoundException();
        }
        return reviewRepository.findReviewsByBookIsbn(bookIsbn, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviewsByCustomerNo(Long customerNo, Integer pageSize, Integer offset) {
        if (!memberRepository.existsById(customerNo)) {
            throw new MemberNotFoundException();
        }
        return reviewRepository.findReviewsByCustomerNo(customerNo, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewNotFoundException 리뷰가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviews(Integer pageSize, Integer offset) {
        return reviewRepository.findReviews(PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewNotFoundException 리뷰가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewByReviewId(Long reviewId) {
        return reviewRepository.findReview(reviewId).orElseThrow(ReviewNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 존재하지 않을 때 발생하는 exception 입니다.
     * @throws BookNotFoundException   도서가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional
    public ReviewResponseDto createReview(CreateReviewRequestDto createReviewRequestDto) {
        Member member = memberRepository.findById(createReviewRequestDto.getCustomerNo()).orElseThrow(MemberNotFoundException::new);

        Book book = bookRepository.findById(createReviewRequestDto.getBookIsbn()).orElseThrow(BookNotFoundException::new);

        Review review = Review.builder()
                .reviewContent(createReviewRequestDto.getReviewContent())
                .reviewImage(createReviewRequestDto.getReviewImage())
                .reviewScore(createReviewRequestDto.getReviewScore())
                .book(bookRepository.getReferenceById(createReviewRequestDto.getBookIsbn()))
                .member(memberRepository.getReferenceById(createReviewRequestDto.getCustomerNo()))
                .createdAt(LocalDate.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .reviewId(savedReview.getReviewId())
                .reviewContent(savedReview.getReviewContent())
                .reviewImage(getReviewImage(savedReview.getReviewImage()))
                .reviewScore(savedReview.getReviewScore())
                .bookIsbn(book.getBookIsbn())
                .customerNo(member.getCustomerNo())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 존재하지 않을 때 발생하는 exception 입니다.
     * @throws BookNotFoundException   도서가 존재하지 않을 때 발생하는 exception 입니다.
     * @throws ReviewNotFoundException 리뷰가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional
    public ReviewResponseDto modifyReview(ModifyReviewRequestDto modifyReviewRequestDto) {
        Member member = memberRepository.findById(modifyReviewRequestDto.getCustomerNo()).orElseThrow(MemberNotFoundException::new);

        Book book = bookRepository.findById(modifyReviewRequestDto.getBookIsbn()).orElseThrow(BookNotFoundException::new);

        Review review = reviewRepository.findById(modifyReviewRequestDto.getReviewId()).orElseThrow(ReviewNotFoundException::new);

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .reviewId(savedReview.getReviewId())
                .reviewContent(savedReview.getReviewContent())
                .reviewImage(getReviewImage(savedReview.getReviewImage()))
                .reviewScore(savedReview.getReviewScore())
                .bookIsbn(book.getBookIsbn())
                .customerNo(member.getCustomerNo())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private String getReviewImage(String reviewImage) {
        if (Objects.isNull(reviewImage)) {
            return null;
        }
        return reviewImage;
    }
}
