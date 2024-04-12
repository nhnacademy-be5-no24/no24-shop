package com.nhnacademy.shop.book.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.tag.domain.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서관리 도서 생성 DTO
 *
 * @author : 이재원
 * @date : 2024-03-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BookCreateRequestDto {


    @JsonProperty("book_isbn")
    private String bookIsbn;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("book_description")
    private String bookDescription;

    @JsonProperty("book_publisher")
    private String bookPublisher;

    @JsonProperty("book_published_at")
    private LocalDate publishedAt;

    @JsonProperty("book_fixed_price")
    private int bookFixedPrice;

    @JsonProperty("book_sale_price")
    private int bookSalePrice;

    @JsonProperty("book_is_packing")
    private boolean bookIsPacking;

    @JsonProperty("book_views")
    private Long bookViews;

    @JsonProperty("book_status")
    private int bookStatus;

    @JsonProperty("book_quantity")
    private int bookQuantity;

    @JsonProperty("book_image")
    private String bookImage;

    private List<Tag> tags;

    private List<Author> authors;

    private List<Category> categories;

    private Long likes;
}
