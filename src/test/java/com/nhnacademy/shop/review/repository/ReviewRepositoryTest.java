package com.nhnacademy.shop.review.repository;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.review.domain.Review;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;
    private Member member;
    Customer customer;
    private Book book;
    private Review review;
    private Grade grade;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GradeRepository gradeRespository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .accumulateRate(5L)
                .build();

        grade = gradeRespository.save(grade);

        customer = Customer.builder()
                .customerNo(1L)
                .customerId("123")
                .customerPassword("1234")
                .customerName("홍길동")
                .customerPhoneNumber("01051745441")
                .customerEmail("jin@naver.com")
                .customerBirthday(LocalDate.of(2001, 2, 21))
                .customerRole("ROLE_MEMBER")
                .build();

        customer = customerRepository.save(customer);

        member = Member.builder()
                .memberId("123")
                .customer(customer)
                .lastLoginAt(LocalDateTime.of(2024, 4, 4, 10, 42))
                .grade(grade)
                .role("ROLE_MEMBER")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        member = memberRepository.save(member);

        book = Book.builder()
                .bookIsbn("1AB")
                .bookTitle("Ant")
                .bookDesc("hi")
                .bookPublisher("가나다")
                .bookPublishedAt(LocalDate.of(2019, 4, 4))
                .bookFixedPrice(10000L)
                .bookSalePrice(9000L)
                .bookIsPacking(true)
                .bookViews(0L)
                .bookStatus(1)
                .bookQuantity(2000)
                .bookImage("image.png")
                .build();

        book = bookRepository.save(book);

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


    @AfterEach
    public void teardown() {
        this.entityManager.createNativeQuery("ALTER TABLE review ALTER COLUMN review_id RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE customer ALTER COLUMN customer_no RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE grade ALTER COLUMN grade_id RESTART WITH 1").executeUpdate();
    }
}
