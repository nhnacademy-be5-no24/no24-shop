package com.nhnacademy.shop.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.dto.response.BookResponseList;
import com.nhnacademy.shop.book.dto.response.BookResponsePage;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.service.BookService;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.tag.domain.Tag;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(
        {RedisConfig.class}
)
class BookControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    private ObjectMapper objectMapper;
    private Book book;
    private Author author;
    private List<BookCategory> bookCategory = new ArrayList<>();
    private List<BookTag> bookTag = new ArrayList<>();
    private Category parentCategory;
    private Category childCategory;
    private Tag tag;
    private Pageable pageable;
    Integer pageSize;
    Integer offset;
    CategoryResponseDto parentCategoryResponseDto;
    CategoryResponseDto childCategoryResponseDto;
    TagResponseDto tagResponseDto;
    List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
    List<TagResponseDto> tagResponseDtos = new ArrayList<>();
    BookCreateRequestDto bookCreateRequestDto;
    BookRequestDto bookRequestDto;
    List<BookResponseDto> bookResponseDtos = new ArrayList<>();
    BookResponseList bookResponseList;
    BookResponsePage bookResponsePage;
    BookResponseDto bookResponseDto;
    Page<BookResponseDto> bookResponseDtoPage;
    Page<Book> bookPage;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BookController(bookService)).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        pageSize = 0;
        offset = 10;

        parentCategory = Category.builder()
                .categoryId(1L)
                .categoryName("유아")
                .parentCategory(null)
                .build();

        childCategory = Category.builder()
                .categoryId(2L)
                .categoryName("그림책")
                .parentCategory(parentCategory)
                .build();

        tag = Tag.builder()
                .tagId(1L)
                .tagName("색칠")
                .build();

        book = Book.builder()
                .bookIsbn("abc")
                .bookTitle("색칠하면서")
                .bookDesc("색칠놀이")
                .bookPublisher("책세상")
                .bookPublishedAt(LocalDate.of(2019, 4, 4))
                .bookFixedPrice(10000L)
                .bookSalePrice(9000L)
                .bookIsPacking(true)
                .bookViews(0L)
                .bookStatus(1)
                .bookQuantity(15)
                .bookImage("image.png")
                .categories(bookCategory)
                .tags(bookTag)
                .author("신유나")
                .likes(2L)
                .build();

        author = Author.builder()
                .authorId(1L)
                .authorName("신유나")
                .build();

        BookCategory.Pk bookCategoryPk = new BookCategory.Pk("abc", 2L);
        bookCategory.add(new BookCategory(bookCategoryPk, book, childCategory));

        BookTag.Pk bookTagPk = new BookTag.Pk("abc", 1L);
        bookTag.add(new BookTag(bookTagPk, book, tag));

        pageable = PageRequest.of(0, 10);

        parentCategoryResponseDto = new CategoryResponseDto(parentCategory.getCategoryId(), parentCategory.getCategoryName(), null);
        childCategoryResponseDto = new CategoryResponseDto(childCategory.getCategoryId(), childCategory.getCategoryName(), parentCategoryResponseDto);
        tagResponseDto = new TagResponseDto(tag.getTagId(), tag.getTagName());
        categoryResponseDtos.add(childCategoryResponseDto);
        categoryResponseDtos.add(parentCategoryResponseDto);
        tagResponseDtos.add(tagResponseDto);
        bookCreateRequestDto = new BookCreateRequestDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(), book.getBookImage(), tagResponseDtos, author.getAuthorName(), categoryResponseDtos, book.getLikes());
        bookRequestDto = new BookRequestDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(), book.getBookImage(), author.getAuthorName(), book.getLikes());
        bookResponseDto = new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(), book.getBookImage(), tagResponseDtos, author.getAuthorName(), book.getLikes());
        bookResponseDtos.add(bookResponseDto);
        bookResponseList = new BookResponseList(bookResponseDtos, 1);
        bookResponsePage = new BookResponsePage(bookResponseDtos, 1);
        bookResponseDtoPage = new PageImpl<>(List.of(bookResponseDto), pageable, 1);
        bookPage = new PageImpl<>(List.of(book), pageable, 1);
    }

    @Test
    @Order(1)
    @DisplayName(value = "도서 생성 성공")
    void createBookTest_Success() {
        when(bookService.createBook(bookCreateRequestDto)).thenReturn(bookResponseDto);
        try {
            mockMvc.perform(post("/shop/books/page")
                            .content(objectMapper.writeValueAsString(bookCreateRequestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(2)
    @DisplayName(value = "도서 생성 실패 - BadRequest")
    void createBookTest_BadRequest() {
        when(bookService.createBook(bookCreateRequestDto)).thenReturn(bookResponseDto);
        try {
            mockMvc.perform(post("/shop/books/page")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName(value = "도서 삭제 성공")
    void deleteBookTest_Success() {
        when(bookService.deleteBook(book.getBookIsbn())).thenReturn(bookResponseDto);
        try {
            mockMvc.perform(delete("/shop/books/delete/abc")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName(value = "도서 삭제 실패 - BookNotFound")
    void deleteBookTest_BookNotFound() {
        when(bookService.deleteBook(book.getBookIsbn())).thenReturn(bookResponseDto);
        try {
            mockMvc.perform(delete("/shop/books/delete/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    @DisplayName(value = "도서 전체 조회 성공")
    void getAllBooksTest_Success() {
        when(bookService.findAllBooks(pageSize, offset)).thenReturn(bookResponseDtoPage);
        try {
            mockMvc.perform(get("/shop/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pageSize", objectMapper.writeValueAsString(pageSize))
                            .param("offset", objectMapper.writeValueAsString(offset)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName(value = "도서 전체 조회 실패 - BookNotFound")
    void getAllBooksTest_BookNotFound() {
        when(bookService.findAllBooks(pageSize, offset)).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pageSize", objectMapper.writeValueAsString(pageSize))
                            .param("offset", objectMapper.writeValueAsString(offset)))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName(value = "도서 단건 조회 성공(BookIsbn)")
    void getByIsbnTest_Success() {
        when(bookService.findByIsbn(book.getBookIsbn())).thenReturn(bookResponseDto);
        try {
            mockMvc.perform(get("/shop/books/isbn/abc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName(value = "도서 단건 조회 실패(BookIsbn) - BookNotFound")
    void getByIsbnTest_BookNotFound() {
        when(bookService.findByIsbn(book.getBookIsbn())).thenReturn(bookResponseDto);
        try {
            mockMvc.perform(get("/shop/books/isbn/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(9)
    @DisplayName(value = "도서 목록 조회 성공(categoryId)")
    void getBookByCategoryTest_Success() {
        when(bookService.findByCategoryId(pageable, childCategory.getCategoryId())).thenReturn(bookResponseDtoPage);
        try {
            mockMvc.perform(get("/shop/books/category/2")
                            .param("pageSize", objectMapper.writeValueAsString(pageSize))
                            .param("offset", objectMapper.writeValueAsString(offset))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    @DisplayName(value = "도서 목록 수정 성공")
    void modifyBookTest_Success() {
        when(bookService.modifyBook(any())).thenReturn(bookResponseDto);

        try {
            mockMvc.perform(put("/shop/books")
                            .content(objectMapper.writeValueAsString(bookRequestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(11)
    @DisplayName(value = "도서 목록 수정 실패 - BookNotFound")
    void modifyBookTest_BookNotFound() {
        when(bookService.modifyBook(any())).thenReturn(null);

        try {
            mockMvc.perform(put("/shop/books")
                            .content(objectMapper.writeValueAsString(bookRequestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(12)
    @DisplayName(value = "도서 목록 수정 성공")
    void modifyBookStatusTest_Success() {
        when(bookService.modifyBookStatus(book.getBookIsbn(), book.getBookStatus())).thenReturn(bookResponseDto);

        try {
            mockMvc.perform(put("/shop/books/status")
                            .param("bookIsbn", book.getBookIsbn())
                            .param("bookStatus", String.valueOf(book.getBookStatus()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(13)
    @DisplayName(value = "도서 목록 수정 실패")
    void modifyBookStatusTest_BookNotFound() {
        when(bookService.modifyBookStatus(book.getBookIsbn(), book.getBookStatus())).thenReturn(null);

        try {
            mockMvc.perform(put("/shop/books/status")
                            .param("bookIsbn", book.getBookIsbn())
                            .param("bookStatus", String.valueOf(book.getBookStatus()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
