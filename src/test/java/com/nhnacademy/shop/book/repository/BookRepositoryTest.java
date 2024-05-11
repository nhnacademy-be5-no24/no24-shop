package com.nhnacademy.shop.book.repository;

import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.book_tag.repository.BookTagRespository;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.tag.domain.Tag;
import com.nhnacademy.shop.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private BookTagRespository bookTagRespository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    private Book book;
    private List<BookCategory> bookCategory = new ArrayList<>();
    private List<BookTag> bookTag = new ArrayList<>();
    private Category parentCategory;
    private Category childCategory;
    private Tag tag;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        parentCategory = Category.builder()
                .categoryId(1L)
                .categoryName("유아")
                .build();

        parentCategory = categoryRepository.save(parentCategory);

        childCategory = Category.builder()
                .categoryId(2L)
                .categoryName("그림책")
                .parentCategory(parentCategory)
                .build();

        childCategory = categoryRepository.save(childCategory);

        tag = Tag.builder()
                .tagId(1L)
                .tagName("색칠")
                .build();

        tag = tagRepository.save(tag);

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

        BookCategory.Pk bookCategoryPk = new BookCategory.Pk("abc", 2L);
        bookCategory.add(new BookCategory(bookCategoryPk, book, childCategory));

        BookTag.Pk bookTagPk = new BookTag.Pk("abc", 1L);
        bookTag.add(new BookTag(bookTagPk, book, tag));

        pageable = PageRequest.of(0, 10);
    }


    @Test
    @Order(1)
    @DisplayName(value = "도서 단건 조회 (도서 고유 번호)")
    void testFindByBookIsbn() {
        bookRepository.save(book);

        Optional<Book> optionalBook = bookRepository.findByBookIsbn(book.getBookIsbn());

        assertThat(optionalBook).isNotEmpty();
        assertThat(optionalBook.get().getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(optionalBook.get().getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(optionalBook.get().getBookDesc()).isEqualTo(book.getBookDesc());
        assertThat(optionalBook.get().getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(optionalBook.get().getBookPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(optionalBook.get().getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(optionalBook.get().getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(optionalBook.get().isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(optionalBook.get().getBookViews()).isEqualTo(book.getBookViews());
        assertThat(optionalBook.get().getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(optionalBook.get().getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(optionalBook.get().getBookImage()).isEqualTo(book.getBookImage());
        assertThat(optionalBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(optionalBook.get().getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(2)
    @DisplayName(value = "도서 전체 조회")
    void testFindAllBooks() {
        bookRepository.save(book);

        Page<BookResponseDto> bookResponseDtos = bookRepository.findAllBooks(pageable);
        List<BookResponseDto> bookList = bookResponseDtos.getContent();

        assertThat(bookList).isNotEmpty();
        assertThat(bookList.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(bookList.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(bookList.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(bookList.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(bookList.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(bookList.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(bookList.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(bookList.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(bookList.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(bookList.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(bookList.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(bookList.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(bookList.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookList.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(3)
    @DisplayName(value = "도서 목록 조회 (도서 제목)")
    void testFindBooksByBookTitle() {
        bookRepository.save(book);

        Page<BookResponseDto> bookResponseDtos = bookRepository.findBooksByBookTitle(pageable, book.getBookTitle());
        List<BookResponseDto> bookList = bookResponseDtos.getContent();

        assertThat(bookList).isNotEmpty();
        assertThat(bookList.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(bookList.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(bookList.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(bookList.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(bookList.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(bookList.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(bookList.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(bookList.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(bookList.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(bookList.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(bookList.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(bookList.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(bookList.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookList.get(0).getLikes()).isEqualTo(book.getLikes());
    }

    @Test
    @Order(4)
    @DisplayName(value = "도서 목록 조회 (도서 설명)")
    void testFindBooksByBookDesc() {
        bookRepository.save(book);

        Page<BookResponseDto> bookResponseDtos = bookRepository.findBooksByBookDesc(pageable, book.getBookDesc());
        List<BookResponseDto> bookList = bookResponseDtos.getContent();

        assertThat(bookList).isNotEmpty();
        assertThat(bookList.get(0).getBookIsbn()).isEqualTo(book.getBookIsbn());
        assertThat(bookList.get(0).getBookTitle()).isEqualTo(book.getBookTitle());
        assertThat(bookList.get(0).getBookDescription()).isEqualTo(book.getBookDesc());
        assertThat(bookList.get(0).getBookPublisher()).isEqualTo(book.getBookPublisher());
        assertThat(bookList.get(0).getPublishedAt()).isEqualTo(book.getBookPublishedAt());
        assertThat(bookList.get(0).getBookFixedPrice()).isEqualTo(book.getBookFixedPrice());
        assertThat(bookList.get(0).getBookSalePrice()).isEqualTo(book.getBookSalePrice());
        assertThat(bookList.get(0).isBookIsPacking()).isEqualTo(book.isBookIsPacking());
        assertThat(bookList.get(0).getBookViews()).isEqualTo(book.getBookViews());
        assertThat(bookList.get(0).getBookStatus()).isEqualTo(book.getBookStatus());
        assertThat(bookList.get(0).getBookQuantity()).isEqualTo(book.getBookQuantity());
        assertThat(bookList.get(0).getBookImage()).isEqualTo(book.getBookImage());
        assertThat(bookList.get(0).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookList.get(0).getLikes()).isEqualTo(book.getLikes());
    }


}
