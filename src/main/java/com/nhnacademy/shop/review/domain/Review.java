package com.nhnacademy.shop.review.domain;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 리뷰(Review) 테이블 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-01
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_image")
    private String reviewImage;

    @Column(name = "review_score", nullable = false)
    private Integer reviewScore;

    @ManyToOne
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customer_no", nullable = false)
    private Member member;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
