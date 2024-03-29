package com.nhnacademy.shop.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Getter
@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_isbn")
    private String bookIsbn;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_desc")
    private String bookDesc;

    @Column(name = "book_publisher")
    private String bookPublisher;

    @Column(name = "book_publish_at")
    private LocalDate bookPublishAt;

    @Column(name = "book_fixed_price")
    private Long bookFixedPrice;

    @Column(name = "book_sale_price")
    private Long bookSalePrice;

    @Column(name = "book_is_packing")
    private boolean bookIsPacking;

    @Column(name = "book_views")
    private Long bookViews;

    @Column(name = "book_status")
    private Integer bookStatus;

    @Column(name = "book_quantity")
    private Long bookQuantity;

    @Column(name = "book_image")
    private String bookImage;
}