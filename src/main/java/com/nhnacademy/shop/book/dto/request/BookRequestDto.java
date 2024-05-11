package com.nhnacademy.shop.book.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.book_tag.dto.response.BookTagResponseDto;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 도서관리 Request DTO
 *
 * @author : 이재원
 * @date : 2024-03-27
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {
    @JsonProperty("book_isbn")
    private String bookIsbn;

    @JsonProperty("book_title")
    private String bookTitle;

    @JsonProperty("book_description")
    private String bookDescription;

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

//    private List<BookTagResponseDto> tags;

    @JsonProperty("author")
    private String author;

//    private List<CategoryResponseDto> categories;

    private Long likes;

}
