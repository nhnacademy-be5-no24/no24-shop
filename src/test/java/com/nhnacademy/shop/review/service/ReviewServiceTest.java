package com.nhnacademy.shop.review.service;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.review.domain.Review;
import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.review.exception.ReviewNotFoundException;
import com.nhnacademy.shop.review.repository.ReviewRepository;
import com.nhnacademy.shop.review.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Review Service 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-01
 */
class ReviewServiceTest {
    private ReviewRepository reviewRepository;
    private MemberRepository memberRepository;
    private BookRepository bookRepository;
    private ReviewService reviewService;
    Review review;
    Member member;
    Book book;
    Grade grade;
    Pageable pageable;
    Integer pageSize;
    Integer offset;
    CreateReviewRequestDto createReviewRequestDto;
    ModifyReviewRequestDto modifyReviewRequestDto;
    ReviewResponseDto reviewResponseDto;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        memberRepository = mock(MemberRepository.class);
        bookRepository = mock(BookRepository.class);
        reviewService = new ReviewServiceImpl(reviewRepository, bookRepository, memberRepository);
        pageable = PageRequest.of(0, 10);
        pageSize = 0;
        offset = 10;

        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .accumulateRate(5L)
                .build();

        book = Book.builder()
                .bookIsbn("ABC123")
                .bookTitle("The Little Prince")
                .bookDesc("hi")
                .bookPublishedAt(LocalDate.now())
                .bookFixedPrice(10000L)
                .bookSalePrice(10000L)
                .bookIsPacking(true)
                .bookViews(203L)
                .bookStatus(1)
                .bookQuantity(15)
                .bookImage("img.png")
                .build();

        member = Member.builder()
                .memberId("abc111")
                .customerNo(1L)
                .lastLoginAt(null)
                .grade(grade).build();

        review = new Review(1L, "nice", "abc", 5, book, member);
        createReviewRequestDto = new CreateReviewRequestDto("good", 3, "abc", "ABC123", 1L);
        modifyReviewRequestDto = new ModifyReviewRequestDto(1L, "bad", 2, "abc", "ABC123", 1L);
        reviewResponseDto = new ReviewResponseDto(1L, "nice", 5, "abc", "ABC123", 1L);
    }

    @Test
    @Order(1)
    @DisplayName(value = "리뷰 단건 조회 성공")
    void getReviewTest() {
        when(reviewRepository.save(any())).thenReturn(review);
        when(reviewRepository.findReview(anyLong())).thenReturn(Optional.of(reviewResponseDto));

        ReviewResponseDto dto  = reviewService.getReviewByReviewId(anyLong());

        verify(reviewRepository, times(1)).findReview(anyLong());

        assertThat(dto).isNotNull();
        assertThat(dto.getReviewId()).isEqualTo(review.getReviewId());
        assertThat(dto.getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(dto.getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(dto.getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(dto.getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(dto.getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(2)
    @DisplayName(value = "리뷰 단건 조회 실패 - ReviewNotFoundException")
    void getReviewTest_NotFound() {
        when(reviewRepository.findReview(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewByReviewId(anyLong()))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining("해당 리뷰를 찾을 수 없습니다.");
    }

    @Test
    @Order(3)
    @DisplayName(value = "리뷰 전체 조회 성공")
    void getReviewsTest() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(1L, "nice", 5, "abc", book.getBookIsbn(), member.getCustomerNo());
        Page<ReviewResponseDto> reviewPage = new PageImpl<>(List.of(reviewResponseDto), pageable, 1);

        when(reviewRepository.findReviews(any())).thenReturn(reviewPage);

        Page<ReviewResponseDto> dtoPage = reviewService.getReviews(pageSize, offset);
        List<ReviewResponseDto> reviewList = dtoPage.getContent();

        verify(reviewRepository, times(1)).findReviews(pageable);

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
    @DisplayName(value = "리뷰 목록 조회 성공 (도서 고유 번호)")
    void getReviewsByBookIsbnTest() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(1L, "nice", 5, "abc", book.getBookIsbn(), member.getCustomerNo());
        Page<ReviewResponseDto> reviewPage = new PageImpl<>(List.of(reviewResponseDto), pageable, 1);

        when(reviewRepository.findReviewsByBookIsbn(anyString(), any())).thenReturn(reviewPage);
        when(bookRepository.existsById(review.getBook().getBookIsbn())).thenReturn(true);

        Page<ReviewResponseDto> dtoPage = reviewService.getReviewsByBookIsbn(review.getBook().getBookIsbn(), pageSize, offset);
        List<ReviewResponseDto> reviewList = dtoPage.getContent();

        verify(reviewRepository, times(1)).findReviewsByBookIsbn(review.getBook().getBookIsbn(), pageable);

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getReviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewList.get(0).getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(reviewList.get(0).getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(reviewList.get(0).getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(reviewList.get(0).getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(reviewList.get(0).getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(5)
    @DisplayName(value = "리뷰 목록 조회 실패 (도서 고유 번호) - BookNotFound")
    void getReviewsByBookIsbnTest_BookNotFound() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(1L, "nice", 5, "abc", book.getBookIsbn(), member.getCustomerNo());
        Page<ReviewResponseDto> reviewPage = new PageImpl<>(List.of(reviewResponseDto), pageable, 1);

        when(reviewRepository.findReviewsByBookIsbn(anyString(), any())).thenReturn(reviewPage);
        when(bookRepository.existsById(review.getBook().getBookIsbn())).thenReturn(false);

        assertThatThrownBy(() -> reviewService.getReviewsByBookIsbn(anyString(), pageSize, offset))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(6)
    @DisplayName(value = "리뷰 목록 조회 성공 (회원 번호)")
    void getReviewsByCustomerNoTest() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(1L, "nice", 5, "abc", book.getBookIsbn(), member.getCustomerNo());
        Page<ReviewResponseDto> reviewPage = new PageImpl<>(List.of(reviewResponseDto), pageable, 1);

        when(reviewRepository.findReviewsByCustomerNo(anyLong(), any())).thenReturn(reviewPage);
        when(memberRepository.existsById(review.getMember().getCustomerNo())).thenReturn(true);

        Page<ReviewResponseDto> dtoPage = reviewService.getReviewsByCustomerNo(review.getMember().getCustomerNo(), pageSize, offset);
        List<ReviewResponseDto> reviewList = dtoPage.getContent();

        verify(reviewRepository, times(1)).findReviewsByCustomerNo(review.getMember().getCustomerNo(), pageable);

        assertThat(reviewList).isNotEmpty();
        assertThat(reviewList.get(0).getReviewId()).isEqualTo(review.getReviewId());
        assertThat(reviewList.get(0).getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(reviewList.get(0).getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(reviewList.get(0).getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(reviewList.get(0).getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(reviewList.get(0).getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(7)
    @DisplayName(value = "리뷰 목록 조회 실패 (회원 번호) - BookNotFound")
    void getReviewsByCustomerNoTest_MemberNotFound() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(1L, "nice", 5, "abc", book.getBookIsbn(), member.getCustomerNo());
        Page<ReviewResponseDto> reviewPage = new PageImpl<>(List.of(reviewResponseDto), pageable, 1);

        when(reviewRepository.findReviewsByBookIsbn(anyString(), any())).thenReturn(reviewPage);
        when(memberRepository.existsById(review.getMember().getCustomerNo())).thenReturn(false);

        assertThatThrownBy(() -> reviewService.getReviewsByCustomerNo(anyLong(), pageSize, offset))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @Test
    @Order(8)
    @DisplayName(value = "리뷰 생성 성공")
    void createReviewTest() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));
        when(reviewRepository.save(any())).thenReturn(review);

        ReviewResponseDto dto = reviewService.createReview(createReviewRequestDto);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).findById(anyString());
        verify(reviewRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getReviewId()).isEqualTo(review.getReviewId());
        assertThat(dto.getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(dto.getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(dto.getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(dto.getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(dto.getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(9)
    @DisplayName(value = "리뷰 생성 실패 - MemberNotFound")
    void createReviewTest_MemberNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.createReview(createReviewRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @Test
    @Order(10)
    @DisplayName(value = "리뷰 생성 실패 - BookNotFound")
    void createReviewTest_BookNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(review.getMember()));
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.createReview(createReviewRequestDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(11)
    @DisplayName(value = "리뷰 수정 성공")
    void modifyReviewTest() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any())).thenReturn(review);

        ReviewResponseDto dto = reviewService.modifyReview(modifyReviewRequestDto);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).findById(anyString());
        verify(reviewRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getReviewId()).isEqualTo(review.getReviewId());
        assertThat(dto.getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(dto.getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(dto.getReviewImage()).isEqualTo(review.getReviewImage());
        assertThat(dto.getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(dto.getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

    @Test
    @Order(12)
    @DisplayName(value = "리뷰 수정 실패 - MemberNotFound")
    void modifyReviewTest_MemberNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.modifyReview(modifyReviewRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @Test
    @Order(13)
    @DisplayName(value = "리뷰 수정 실패 - BookNotFound")
    void modifyReviewTest_BookNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(review.getMember()));
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.modifyReview(modifyReviewRequestDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(14)
    @DisplayName(value = "리뷰 수정 실패 - ReviewNotFound")
    void modifyReviewTest_ReviewNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(review.getMember()));
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(review.getBook()));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.modifyReview(modifyReviewRequestDto))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining("해당 리뷰를 찾을 수 없습니다.");
    }

    @Test
    @Order(15)
    @DisplayName(value = "리뷰 삭제 성공")
    void deleteReviewTest() {
        reviewService.deleteReview(review.getReviewId());

        verify(reviewRepository, times(1)).deleteById(review.getReviewId());
    }

    @Test
    @Order(16)
    @DisplayName(value = "리뷰 이미지 Null")
    void getReviewImageTest() {
        ReflectionTestUtils.setField(createReviewRequestDto, "reviewImage", null);
        ReflectionTestUtils.setField(review, "reviewImage", null);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));
        when(reviewRepository.save(any())).thenReturn(review);

        ReviewResponseDto dto = reviewService.createReview(createReviewRequestDto);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).findById(anyString());
        verify(reviewRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getReviewId()).isEqualTo(review.getReviewId());
        assertThat(dto.getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(dto.getReviewScore()).isEqualTo(review.getReviewScore());
        assertThat(dto.getReviewImage()).isNull();
        assertThat(dto.getBookIsbn()).isEqualTo(review.getBook().getBookIsbn());
        assertThat(dto.getCustomerNo()).isEqualTo(review.getMember().getCustomerNo());
    }

}
