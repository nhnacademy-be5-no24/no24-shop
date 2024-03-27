package com.nhnacademy.shop.book.domain;

import lombok.*;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column(name = "book_isbn")
    private String bookIsbn;

    @Column(name = "book_title")
    private String bookTittle;

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

    /*
    book status
    0 : 판매중
    1 : 수량부족
    2 : 판매종료
    3 : 삭제된 도서
     */
    @Column(name = "book_status")
    private Integer bookStatus;

    @Column(name = "book_quantity")
    private Long bookQuantity;

    @Column(name = "book_image")
    private String bookImage;

}
