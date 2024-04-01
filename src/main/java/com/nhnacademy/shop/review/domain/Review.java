package com.nhnacademy.shop.review.domain;

import com.nhnacademy.shop.book.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
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

    @Column(name = "customer_no", nullable = false)
    private Integer customerNo;
}