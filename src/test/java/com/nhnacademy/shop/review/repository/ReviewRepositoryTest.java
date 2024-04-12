package com.nhnacademy.shop.review.repository;

import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.review.domain.Review;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Review Repository 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-01
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles(value = "dev")
@Transactional
@WebAppConfiguration
class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;
    private Member member;
    private Book book;
    private Review review;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .memberId("123")
                .customerNo(1L)
                .lastLoginAt(null)
                .gradeId(1L)
                .build();

        book = Book.builder()
                .bookIsbn("1AB")
                .bookTitle("Ant")
                .bookDesc("hi")
                .build();

        review = Review.builder()
                .reviewId(1L)
                .reviewImage(null)
                .reviewContent("good")
                .reviewScore(3)
                .book(book)
                .member(member)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName(value = "리뷰 목록 조회 (도서 고유 번호)")
    void testFindReviewsByBook_BookIsbn() {

        memberRepository.save(member);

        bookRepository.save(book);

        reviewRepository.save(review);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ReviewResponseDto> dtoPage = reviewRepository.findReviewsByBookIsbn(review.getBook().getBookIsbn(), pageable);
        List<ReviewResponseDto> reviewList = dtoPage.getContent();

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getReviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewList.get(0).getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(reviewList.get(0).getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(reviewList.get(0).getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(reviewList.get(0).getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(reviewList.get(0).getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(2)
    @DisplayName(value = "리뷰 목록 조회 (회원 번호)")
    void testFindReviewsByMember_CustomerNo() {

        memberRepository.save(member);

        bookRepository.save(book);

        review = reviewRepository.save(review);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ReviewResponseDto> dtoPage = reviewRepository.findReviewsByCustomerNo(review.getMember().getCustomerNo(), pageable);
        List<ReviewResponseDto> reviewList = dtoPage.getContent();

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getReviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewList.get(0).getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(reviewList.get(0).getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(reviewList.get(0).getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(reviewList.get(0).getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(reviewList.get(0).getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(3)
    @DisplayName(value = "전체 리뷰 조회")
    void testFindReviews() {

        memberRepository.save(member);

        bookRepository.save(book);

        review = reviewRepository.save(review);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ReviewResponseDto> dtoPage = reviewRepository.findReviews(pageable);
        List<ReviewResponseDto> reviewList = dtoPage.getContent();

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getReviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewList.get(0).getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(reviewList.get(0).getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(reviewList.get(0).getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(reviewList.get(0).getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(reviewList.get(0).getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(4)
    @DisplayName(value = "리뷰 단건 조회")
    void testFindReviewsByReviewId() {

        memberRepository.save(member);

        bookRepository.save(book);

        review = reviewRepository.save(review);


        Optional<ReviewResponseDto> reviewDto = reviewRepository.findReview(review.getReviewId());

        assertThat(reviewDto).isNotEmpty();
        assertThat(reviewDto.get().getReviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewDto.get().getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(reviewDto.get().getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(reviewDto.get().getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(reviewDto.get().getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(reviewDto.get().getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }
}
