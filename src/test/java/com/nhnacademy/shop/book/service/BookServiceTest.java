package com.nhnacademy.shop.book.service;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.dto.response.BookResponseList;
import com.nhnacademy.shop.book.dto.response.BookResponsePage;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookAlreadyExistsException;
import com.nhnacademy.shop.book.exception.BookIsDeletedException;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.book.service.impl.BookServiceImpl;
import com.nhnacademy.shop.book_author.repository.BookAuthorRepository;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.book_tag.repository.BookTagRespository;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.tag.domain.Tag;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import com.nhnacademy.shop.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BookServiceTest {
    private BookRepository bookRepository;
    private BookCategoryRepository bookCategoryRepository;
    private BookTagRespository bookTagRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;
    private AuthorRepository authorRepository;
    private BookAuthorRepository bookAuthorRepository;
    private BookService bookService;
    private Book book;
    private Book deletedBook;
    private Author author;
    private List<BookCategory> bookCategory = new ArrayList<>();
    private List<BookTag> bookTag = new ArrayList<>();
    private Page<BookCategory> bookCategoryPage;
    private Page<BookTag> bookTagPage;
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
        bookRepository = mock(BookRepository.class);
        bookCategoryRepository = mock(BookCategoryRepository.class);
        bookTagRepository = mock(BookTagRespository.class);
        categoryRepository = mock(CategoryRepository.class);
        tagRepository = mock(TagRepository.class);
        authorRepository = mock(AuthorRepository.class);
        bookAuthorRepository = mock(BookAuthorRepository.class);
        bookService = new BookServiceImpl(bookRepository, categoryRepository, tagRepository, bookCategoryRepository, bookTagRepository, authorRepository, bookAuthorRepository);
        pageSize = 0;
        offset = 10;

        parentCategory = Category.builder()
                .categoryId(1L)
                .categoryName("유아")
                .parentCategory(null)
                .build();

        categoryRepository.save(parentCategory);

        childCategory = Category.builder()
                .categoryId(2L)
                .categoryName("그림책")
                .parentCategory(parentCategory)
                .build();

        categoryRepository.save(childCategory);

        tag = Tag.builder()
                .tagId(1L)
                .tagName("색칠")
                .build();

        tagRepository.save(tag);

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

        deletedBook = Book.builder()
                .bookIsbn("abc")
                .bookTitle("색칠하면서")
                .bookDesc("색칠놀이")
                .bookPublisher("책세상")
                .bookPublishedAt(LocalDate.of(2019, 4, 4))
                .bookFixedPrice(10000L)
                .bookSalePrice(9000L)
                .bookIsPacking(true)
                .bookViews(0L)
                .bookStatus(3)
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

        authorRepository.save(author);

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
        bookResponseList = new BookResponseList(bookResponseDtos);
        bookResponsePage = new BookResponsePage(bookResponseDtos);
        bookResponseDtoPage = new PageImpl<>(List.of(bookResponseDto), pageable, 1);
        bookCategoryPage = new PageImpl<>(List.of(bookCategory.get(0)), pageable, 1);
        bookTagPage = new PageImpl<>(List.of(bookTag.get(0)), pageable, 1);
        bookPage = new PageImpl<>(List.of(book), pageable, 1);
    }

    @Test
    @Order(1)
    @DisplayName(value = "도서 생성 성공")
    void createBookTest_Success() {
        when(bookRepository.save(any())).thenReturn(book);
        when(bookRepository.findByBookIsbn(anyString())).thenReturn(Optional.empty());
        when(bookCategoryRepository.saveAll(any())).thenReturn(bookCategory);
        when(bookTagRepository.saveAll(any())).thenReturn(bookTag);

        BookResponseDto dto = bookService.createBook(bookCreateRequestDto);

        verify(bookRepository, times(1)).findByBookIsbn(anyString());

        assertThat(dto).isNotNull();
        assertThat(dto.getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(dto.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(dto.getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(dto.getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(dto.getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(dto.getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(dto.getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(dto.isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(dto.getBookViews()).isEqualTo(book.getBookViews());
        assertThat(dto.getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(dto.getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(dto.getBookImage()).isEqualTo(book.getBookImage());
        assertThat(dto.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(dto.getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(2)
    @DisplayName(value = "도서 생성 실패 - BookAlreadyExists")
    void createBookTest_BookAlreadyExists() {
        when(bookRepository.findByBookIsbn(anyString())).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> bookService.createBook(bookCreateRequestDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessageContaining("This book is already exist.");
    }

    @Test
    @Order(3)
    @DisplayName(value = "도서 삭제 성공 - 상태 변경")
    void deleteBookTest_Success() {
        when(bookRepository.save(any())).thenReturn(deletedBook);
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));

        BookResponseDto dto = bookService.deleteBook(book.getBookIsbn());

        verify(bookRepository, times(1)).findById(anyString());
        verify(bookRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(dto.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(dto.getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(dto.getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(dto.getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(dto.getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(dto.getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(dto.isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(dto.getBookViews()).isEqualTo(book.getBookViews());
        assertThat(dto.getBookStatus()).isEqualTo(deletedBook.getBookStatus());
        assertThat(dto.getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(dto.getBookImage()).isEqualTo(book.getBookImage());
        assertThat(dto.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(dto.getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(4)
    @DisplayName(value = "도서 삭제 실패 - BookIsDeleted")
    void deleteBookTest_BookIsDeleted() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(deletedBook));

        assertThatThrownBy(() -> bookService.deleteBook(book.getBookIsbn()))
                .isInstanceOf(BookIsDeletedException.class)
                .hasMessageContaining("This book is deleted");
    }

    @Test
    @Order(5)
    @DisplayName(value = "도서 삭제 실패 - BookNotFound")
    void deleteBookTest_BookNotFound() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBook(book.getBookIsbn()))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(6)
    @DisplayName(value = "도서 전체 조회 성공")
    void findAllBooksTest_Success() {
        when(bookRepository.findAllBooks(pageable)).thenReturn(bookResponseDtoPage);

        Page<BookResponseDto> dto = bookService.findAllBooks(pageSize, offset);
        List<BookResponseDto> content = dto.getContent();

        verify(bookRepository, times(1)).findAllBooks(pageable);

        assertThat(content.get(0)).isNotNull();
        assertThat(content.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(content.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(content.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(content.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(content.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(content.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(content.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(content.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(content.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(content.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(content.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(content.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(content.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(content.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(7)
    @DisplayName(value = "도서 전체 조회 실패 - BookNotFound")
    void findAllBooksTest_BookNotFound() {
        when(bookRepository.findAllBooks(pageable)).thenReturn(Page.empty());

        assertThatThrownBy(() -> bookService.findAllBooks(pageSize, offset))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(8)
    @DisplayName(value = "도서 목록 조회 성공 - 카테고리 아이디")
    void findByCategoryIdTest_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(childCategory));
        when(bookCategoryRepository.findByCategory(pageable, childCategory)).thenReturn(bookCategoryPage);

        Page<BookResponseDto> dto = bookService.findByCategoryId(pageable, childCategory.getCategoryId());
        List<BookResponseDto> content = dto.getContent();

        verify(categoryRepository, times(1)).findById(childCategory.getCategoryId());
        verify(bookCategoryRepository, times(1)).findByCategory(pageable, childCategory);

        assertThat(content.get(0)).isNotNull();
        assertThat(content.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(content.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(content.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(content.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(content.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(content.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(content.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(content.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(content.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(content.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(content.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(content.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(content.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(content.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(9)
    @DisplayName(value = "도서 목록 조회 실패 - CategoryNotFound")
    void findByCategoryIdTest_CategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findByCategoryId(pageable, childCategory.getCategoryId()))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    @Order(10)
    @DisplayName(value = "도서 목록 조회 성공 - 태그")
    void findByTagTest_Success() {
        when(tagRepository.findByTagId(anyLong())).thenReturn(tag);
        when(bookTagRepository.findByTag(pageable, tag)).thenReturn(bookPage);

        Page<BookResponseDto> dto = bookService.findByTag(pageable, tag.getTagId());
        List<BookResponseDto> content = dto.getContent();

        verify(tagRepository, times(1)).findByTagId(anyLong());
        verify(bookTagRepository, times(1)).findByTag(pageable, tag);

        assertThat(content.get(0)).isNotNull();
        assertThat(content.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(content.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(content.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(content.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(content.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(content.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(content.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(content.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(content.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(content.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(content.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(content.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(content.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(content.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(11)
    @DisplayName(value = "도서 목록 조회 성공 - 제목")
    void findByTitleTest_Success() {
        when(bookRepository.findBooksByBookTitle(pageable, book.getBookTitle())).thenReturn(bookResponseDtoPage);

        Page<BookResponseDto> dto = bookService.findByTitle(pageable, book.getBookTitle());
        List<BookResponseDto> content = dto.getContent();

        verify(bookRepository, times(1)).findBooksByBookTitle(pageable, book.getBookTitle());

        assertThat(content.get(0)).isNotNull();
        assertThat(content.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(content.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(content.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(content.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(content.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(content.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(content.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(content.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(content.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(content.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(content.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(content.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(content.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(content.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(12)
    @DisplayName(value = "도서 목록 조회 성공 - 작가")
    void findByAuthorTest_Success() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(author));
        when(bookAuthorRepository.findByAuthor(pageable, author)).thenReturn(bookPage);

        Page<BookResponseDto> dto = bookService.findByAuthor(pageable, author.getAuthorId());
        List<BookResponseDto> content = dto.getContent();

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookAuthorRepository, times(1)).findByAuthor(pageable, author);

        assertThat(content.get(0)).isNotNull();
        assertThat(content.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(content.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(content.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(content.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(content.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(content.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(content.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(content.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(content.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(content.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(content.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(content.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(content.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(content.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(13)
    @DisplayName(value = "도서 단건 조회 성공 - 도서 고유 번호")
    void findByIsbnTest_Success() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.ofNullable(book));

        BookResponseDto dto = bookService.findByIsbn(book.getBookIsbn());

        verify(bookRepository, times(1)).findById(anyString());

        assertThat(dto).isNotNull();
        assertThat(dto.getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(dto.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(dto.getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(dto.getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(dto.getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(dto.getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(dto.getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(dto.isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(dto.getBookViews()).isEqualTo(book.getBookViews());
        assertThat(dto.getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(dto.getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(dto.getBookImage()).isEqualTo(book.getBookImage());
        assertThat(dto.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(dto.getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(14)
    @DisplayName(value = "도서 단건 조회 실패 - BookNotFound")
    void findByIsbnTest_BookNotFound() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findByIsbn(book.getBookIsbn()))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(15)
    @DisplayName(value = "도서 목록 조회 성공 - 도서 설명")
    void findByDescriptionTest_Success() {
        when(bookRepository.findBooksByBookDesc(pageable, book.getBookDesc())).thenReturn(bookResponseDtoPage);

        Page<BookResponseDto> dto = bookService.findByDescription(pageable, book.getBookDesc());
        List<BookResponseDto> content = dto.getContent();

        verify(bookRepository, times(1)).findBooksByBookDesc(pageable, book.getBookDesc());

        assertThat(content).isNotNull();
        assertThat(content.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(content.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(content.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(content.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(content.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(content.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(content.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(content.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(content.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(content.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(content.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(content.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(content.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(content.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(16)
    @DisplayName(value = "도서 수정 성공")
    void modifyBookTest_Success() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.ofNullable(book));
        when(bookCategoryRepository.saveAll(any())).thenReturn(bookCategory);
        when(bookRepository.save(any())).thenReturn(book);

        BookResponseDto dto = bookService.modifyBook(bookRequestDto);

        verify(bookRepository, times(1)).findById(book.getBookIsbn());
        verify(bookCategoryRepository, times(1)).saveAll(book.getCategories());
        verify(bookTagRepository, times(1)).saveAll(book.getTags());
        verify(bookRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(dto.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(dto.getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(dto.getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(dto.getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(dto.getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(dto.getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(dto.isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(dto.getBookViews()).isEqualTo(book.getBookViews());
        assertThat(dto.getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(dto.getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(dto.getBookImage()).isEqualTo(book.getBookImage());
        assertThat(dto.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(dto.getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(17)
    @DisplayName(value = "도서 수정 실패 - BookNotFound")
    void modifyBookTest_BookNotFound() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.modifyBook(bookRequestDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }

    @Test
    @Order(18)
    @DisplayName(value = "도서 상태 수정 성공 - 상태 변경")
    void modifyBookStatusTest_Success() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.ofNullable(book));

        BookResponseDto dto = bookService.modifyBookStatus(book.getBookIsbn(), 1);

        verify(bookRepository, times(1)).findById(book.getBookIsbn());
        verify(bookRepository, times(1)).save(any());

        assertThat(dto).isNotNull();
        assertThat(dto.getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(dto.getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(dto.getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(dto.getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(dto.getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(dto.getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(dto.getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(dto.isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(dto.getBookViews()).isEqualTo(book.getBookViews());
        assertThat(dto.getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(dto.getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(dto.getBookImage()).isEqualTo(book.getBookImage());
        assertThat(dto.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(dto.getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(19)
    @DisplayName(value = "도서 상태 수정 실패 - BookNotFound")
    void modifyBookStatusTest_BookNotFound() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.modifyBookStatus(book.getBookIsbn(), 1))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("This book is not found");
    }
}
