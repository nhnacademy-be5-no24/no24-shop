package com.nhnacademy.shop.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.category.controller.CategoryController;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.review.domain.Review;
import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.review.exception.ReviewNotFoundException;
import com.nhnacademy.shop.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Review RestController 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-02
 */
@WebMvcTest(ReviewController.class)
@Import(
        {RedisConfig.class}
)
public class ReviewControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewService;
    private ObjectMapper objectMapper = new ObjectMapper();
    CreateReviewRequestDto createReviewRequestDto;
    ModifyReviewRequestDto modifyReviewRequestDto;
    Page<ReviewResponseDto> reviewPage;
    Review review;
    ReviewResponseDto reviewResponseDto;
    Book book;
    Member member;
    Grade grade;
    Pageable pageable;
    Integer pageSize;
    Integer offset;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService)).build();
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
        reviewPage = new PageImpl<>(List.of(reviewResponseDto), pageable, 1);
    }


    @Test
    @Order(1)
    @DisplayName(value = "리뷰 단건 조회 성공")
    void getReviewTest_Success() {
        when(reviewService.getReviewByReviewId(anyLong())).thenReturn(reviewResponseDto);
        try {
            mockMvc.perform(get("/shop/reviews/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName(value = "리뷰 단건 조회 실패")
    void getReviewTest_NotFound() {
        when(reviewService.getReviewByReviewId(anyLong())).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/reviews/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName(value = "전체 리뷰 조회 성공")
    void getReviewsTest_Success() {
        when(reviewService.getReviews(pageSize, offset)).thenReturn(reviewPage);
        try {
            mockMvc.perform(get("/shop/reviews")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName(value = "리뷰 목록 조회 성공 (도서 고유 번호)")
    void getReviewsByBookIsbnTest_Success() {
        when(reviewService.getReviewsByBookIsbn(review.getBook().getBookIsbn(), pageSize, offset)).thenReturn(reviewPage);
        try {
            mockMvc.perform(get("/shop/reviews/bookIsbn/ABC123")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    @DisplayName(value = "리뷰 목록 조회 실패 (도서 고유 번호) - BookNotFound")
    void getReviewsByBookIsbnTest_BookNotFound() {
        when(reviewService.getReviewsByBookIsbn(anyString(), anyInt(), anyInt())).thenThrow(BookNotFoundException.class);
        try {
            mockMvc.perform(get("/shop/reviews/bookIsbn/ABC123")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName(value = "리뷰 목록 조회 실패 (도서 고유 번호) - ReviewNotFound")
    void getReviewsByBookIsbnTest_ReviewNotFound() {
        when(reviewService.getReviewsByBookIsbn(anyString(), anyInt(), anyInt())).thenThrow(ReviewNotFoundException.class);
        try {
            mockMvc.perform(get("/shop/reviews/bookIsbn/ABC123")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName(value = "리뷰 목록 조회 성공 (회원 번호)")
    void getReviewsByCustomerNoTest_Success() {
        when(reviewService.getReviewsByCustomerNo(review.getMember().getCustomerNo(), pageSize, offset)).thenReturn(reviewPage);
        try {
            mockMvc.perform(get("/shop/reviews/customerNo/1")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName(value = "리뷰 목록 조회 실패 (회원 번호) - MemberNotFound")
    void getReviewsByCustomerNoTest_MemberNotFound() {
        when(reviewService.getReviewsByCustomerNo(anyLong(), anyInt(), anyInt())).thenThrow(MemberNotFoundException.class);
        try {
            mockMvc.perform(get("/shop/reviews/customerNo/1")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(9)
    @DisplayName(value = "리뷰 목록 조회 실패 (회원 번호) - ReviewNotFound")
    void getReviewsByCustomerNoTest_ReviewNotFound() {
        when(reviewService.getReviewsByCustomerNo(anyLong(), anyInt(), anyInt())).thenThrow(ReviewNotFoundException.class);
        try {
            mockMvc.perform(get("/shop/reviews/customerNo/1")
                            .param("pageSize", "10")
                            .param("offset", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    @DisplayName(value = "리뷰 생성 성공")
    void createReviewTest_Success() {
        when(reviewService.createReview(createReviewRequestDto)).thenReturn(reviewResponseDto);
        try {
            mockMvc.perform(post("/shop/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createReviewRequestDto)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(11)
    @DisplayName(value = "리뷰 수정 성공")
    void modifyReviewTest_Success() {
        when(reviewService.modifyReview(modifyReviewRequestDto)).thenReturn(reviewResponseDto);
        try {
            mockMvc.perform(put("/shop/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(modifyReviewRequestDto)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(12)
    @DisplayName(value = "리뷰 삭제 성공")
    void deleteReviewTest_Success() {
        try {
            mockMvc.perform(delete("/shop/reviews/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }


}
