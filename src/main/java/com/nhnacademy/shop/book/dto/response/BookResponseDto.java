package com.nhnacademy.shop.book.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book_author.domain.BookAuthor;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.tag.domain.Tag;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 도서관리 Response DTO
 *
 * @author : 이재원
 * @date : 2024-03-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {

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
    private long bookFixedPrice;

    @JsonProperty("book_sale_price")
    private long bookSalePrice;

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

    private List<TagResponseDto> tags;

    private String author;

    private Long likes;

    public BookResponseDto(Book book) {
        this.bookIsbn = book.getBookIsbn();
        this.bookTitle = book.getBookTitle();
        this.bookDescription = book.getBookDesc();
        this.bookPublisher = book.getBookPublisher();
        this.publishedAt = book.getBookPublishedAt();
        this.bookFixedPrice = book.getBookFixedPrice();
        this.bookSalePrice = book.getBookSalePrice();
        this.bookIsPacking = book.isBookIsPacking();
        this.bookViews = book.getBookViews();
        this.bookStatus = book.getBookStatus();
        this.bookQuantity = book.getBookQuantity();
        this.bookImage = book.getBookImage();
        this.author = book.getAuthor();
        this.likes = book.getLikes();
    }

    public BookResponseDto(String bookIsbn,
                           String bookTitle,
                           String bookDescription,
                           String bookPublisher,
                           LocalDate publishedAt,
                           Long bookFixedPrice,
                           Long bookSalePrice,
                           boolean bookIsPacking,
                           Long bookViews,
                           int bookStatus,
                           int bookQuantity,
                           String bookImage,
                           String author,
                           Long likes) {
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.bookDescription = bookDescription;
        this.bookPublisher = bookPublisher;
        this.publishedAt = publishedAt;
        this.bookFixedPrice = bookFixedPrice;
        this.bookSalePrice = bookSalePrice;
        this.bookIsPacking = bookIsPacking;
        this.bookViews = bookViews;
        this.bookStatus = bookStatus;
        this.bookQuantity = bookQuantity;
        this.bookImage = bookImage;
        this.author = author;
        this.likes = likes;
    }
}
