package com.nhnacademy.shop.book.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.shop.book_tag.dto.response.BookTagResponseDto;

import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import lombok.*;

/**
 * 도서관리 도서 생성 DTO
 *
 * @author : 이재원
 * @date : 2024-03-28
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private Long bookFixedPrice;

    @JsonProperty("book_sale_price")
    private Long bookSalePrice;

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

    @JsonProperty
    private List<TagResponseDto> tags;

    @JsonProperty("book_author")
    private String author;

    @JsonProperty
    private List<CategoryResponseDto> categories;

    private Long likes;
}
