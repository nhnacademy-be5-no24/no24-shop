package com.nhnacademy.shop.book;


import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.tag.domain.Tag;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 도서관리 Entity
 *
 * @author : 이재원
 * @date : 2024-03-27
 */
@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @Column(name = "book_isbn", nullable = false)
    private String bookIsbn;

    @Column(name = "book_title", nullable = false)
    @Length(max = 255)
    private String bookTitle;

    @Column(name = "book_desc", nullable = false)
    private String bookDesc;

    @Column(name = "book_publisher", nullable = false)
    private String bookPublisher;

    @Column(name = "book_publisher_at", nullable = false)
    private LocalDate bookPublisherAt;

    @Column(name = "book_fixed_price", nullable = false)
    private int bookFixedPrice;

    @Column(name = "book_sale_price", nullable = false)
    private int bookSalePrice;

    @Column(name = "book_is_packing", nullable = false)
    private boolean bookIsPacking;

    @Column(name = "book_views", nullable = false)
    private Long bookViews;

    /*
     * Express "book_status" to integer
     * 0 : 판매중
     * 1 : 수량부족
     * 2 : 판매종료
     * 3 : 삭제된 도서
     */
    @Column(name = "book_status", nullable = false)
    private int bookStatus;

    @Column(name = "book_quantity", nullable = false)
    private int bookQuantity;

    @Column(name = "book_image")
    private String bookImage;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Author> authors;

}