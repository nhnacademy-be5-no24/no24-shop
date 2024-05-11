package com.nhnacademy.shop.book.service.impl;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.exception.BookAlreadyExistsException;
import com.nhnacademy.shop.book.exception.BookIsDeletedException;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.book.service.BookService;
import com.nhnacademy.shop.book_author.repository.BookAuthorRepository;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.book_tag.repository.BookTagRespository;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.tag.domain.Tag;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import com.nhnacademy.shop.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 도서관리 Service 구현체
 *
 * @author : 이재원
 * @date : 2024-03-30
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookTagRespository bookTagRespository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;


    /*
     * book 생성
     * @Param : request
     */
    @Override
    @Transactional
    public BookResponseDto createBook(BookCreateRequestDto request) {
        Optional<Book> optionalBook = bookRepository.findByBookIsbn(request.getBookIsbn());
        if (optionalBook.isPresent()) {
            throw new BookAlreadyExistsException();
        }
        // todo : category, tag 수정 필요.
        Book book = Book.builder()
                .bookIsbn(request.getBookIsbn())
                .bookTitle(request.getBookTitle())
                .bookDesc(request.getBookDescription())
                .bookPublisher(request.getBookPublisher())
                .bookPublishedAt(request.getPublishedAt())
                .bookFixedPrice(request.getBookFixedPrice())
                .bookSalePrice(request.getBookSalePrice())
                .bookIsPacking(request.isBookIsPacking())
                .bookViews(0L)
                .bookStatus(request.getBookStatus())
                .bookQuantity(request.getBookQuantity())
                .bookImage(request.getBookImage())
                .tags(null)
                .categories(null)
                .author(request.getAuthor())
                .likes(0L).build();


        Book createdBook = bookRepository.save(book);
        // todo : tag 넣어줘야함.
        return BookResponseDto.builder()
                .bookIsbn(createdBook.getBookIsbn())
                .bookTitle(createdBook.getBookTitle())
                .bookDescription(createdBook.getBookDesc())
                .bookPublisher(createdBook.getBookPublisher())
                .publishedAt(createdBook.getBookPublishedAt())
                .bookFixedPrice(createdBook.getBookFixedPrice())
                .bookSalePrice(createdBook.getBookSalePrice())
                .bookIsPacking(createdBook.isBookIsPacking())
                .bookStatus(createdBook.getBookStatus())
                .bookQuantity(createdBook.getBookQuantity())
                .bookImage(createdBook.getBookImage())
                .bookViews(createdBook.getBookViews())
                .tags(null)
                .author(createdBook.getAuthor())
                .likes(createdBook.getLikes())
                .build();
//        return new BookResponseDto(createdBook.getBookIsbn(), createdBook.getBookTitle(), createdBook.getBookDesc(), createdBook.getBookPublisher(),
//                createdBook.getBookPublishedAt(), createdBook.getBookFixedPrice(), createdBook.getBookSalePrice(), createdBook.isBookIsPacking(), createdBook.getBookViews(), createdBook.getBookStatus(), createdBook.getBookQuantity(), createdBook.getBookImage(),
//                book.getTags().stream()
//                        .map(booktag -> booktag.getTag())
//                        .map(tag -> new TagResponseDto(tag))
//                        .collect(Collectors.toList()),
//                book.getAuthor(), book.getLikes());

    }

    /*
     * book 삭제
     * @Param : bookIsbn
     */
    @Override
    @Transactional
    public BookResponseDto deleteBook(String bookIsbn) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);

        if (book.getBookStatus() == 3) {
            throw new BookIsDeletedException();
        }

        book = bookRepository.save(new Book(bookIsbn, book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), 3, book.getBookQuantity(), book.getBookImage(),
                book.getCategories(), book.getTags(), book.getAuthor(), book.getLikes()));

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(),
                book.getTags().stream()
                        .map(BookTag::getTag)
                        .map(TagResponseDto::new)
                        .collect(Collectors.toList()),
                book.getAuthor(), book.getLikes());
    }

    /*
     * 전체의 book 정보를 조회
     * @Param : pageable
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findAllBooks(Integer pageSize, Integer offset) {
        Page<BookResponseDto> response = bookRepository.findAllBooks(PageRequest.of(pageSize, offset));

        if (response.getContent().isEmpty() || response.getTotalElements() == 0) {
            throw new BookNotFoundException();
        }

        return response;
    }


    /*
     * category에 일치하는 book list 탐색
     * @Param : pageable, categoryId
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findByCategoryId(Pageable pageable, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        Page<BookCategory> bookList = bookCategoryRepository.findByCategory(pageable, category);

        List<BookResponseDto> bookResponseDtoList = bookList.getContent().stream()
                .map(BookCategory::getBook)
                .map(book -> {
                    return BookResponseDto.builder()
                            .bookIsbn(book.getBookIsbn())
                            .bookTitle(book.getBookTitle())
                            .bookDescription(book.getBookDesc())
                            .bookPublisher(book.getBookPublisher())
                            .publishedAt(book.getBookPublishedAt())
                            .bookFixedPrice(book.getBookFixedPrice())
                            .bookSalePrice(book.getBookSalePrice())
                            .bookIsPacking(book.isBookIsPacking())
                            .bookViews(book.getBookViews())
                            .bookStatus(book.getBookStatus())
                            .bookQuantity(book.getBookQuantity())
                            .bookImage(book.getBookImage())
                            .tags(book.getTags().stream()
                                    .map(BookTag::getTag)
                                    .map(TagResponseDto::new)
                                    .collect(Collectors.toList()))
                            .author(book.getAuthor())
                            .likes(book.getLikes())
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtoList, pageable, bookList.getTotalElements());
    }

    /*
     * book tag에 일치하는 book list 탐색
     * @Param : pageable, tagId
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findByTag(Pageable pageable, Long tagId) {
        Tag tag = tagRepository.findByTagId(tagId);

        Page<Book> bookList = bookTagRespository.findByTag(pageable, tag);

        List<BookResponseDto> bookResponseDtoList = bookList.getContent().stream()
                .map(this::findBookByTags)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtoList, pageable, bookList.getTotalElements());
    }

    private BookResponseDto findBookByTags(Book book) {

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(),
                book.getTags().stream()
                        .map(BookTag::getTag)
                        .map(TagResponseDto::new)
                        .collect(Collectors.toList()),
                book.getAuthor(), book.getLikes());
    }

    /*
     * book name에 일치하는 book list 탐색
     * @Param : pageable, bookName
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findByTitle(Pageable pageable, String bookTitle) {
        return bookRepository.findBooksByBookTitle(pageable, bookTitle);
    }

    /*
     * author에 일치하는 book list 탐색
     * @Param : pageable, authorId
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findByAuthor(Pageable pageable, Long authorId) {
        Author author = authorRepository.findById(authorId).get();
        Page<Book> bookList = bookAuthorRepository.findByAuthor(pageable, author);
        List<BookResponseDto> bookResponseDtoList = bookList.getContent().stream()
                .map(this::findBookByAuthors)
                .collect(Collectors.toList());
        return new PageImpl<>(bookResponseDtoList, pageable, bookList.getTotalElements());
    }

    private BookResponseDto findBookByAuthors(Book book) {
        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(),
                book.getTags().stream()
                        .map(BookTag::getTag)
                        .map(TagResponseDto::new)
                        .collect(Collectors.toList()),
                book.getAuthor(), book.getLikes());
    }

    /*
     * book ISBN에 일치하는 book list 탐색
     * @Param : bookIsbn
     */

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto findByIsbn(String bookIsbn) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(),
                book.getTags().stream()
                        .map(BookTag::getTag)
                        .map(TagResponseDto::new)
                        .collect(Collectors.toList()),
                book.getAuthor(), book.getLikes());
    }

    /*
     * book description에 일치하는 book list 탐색
     * @Param : bookDescription
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findByDescription(Pageable pageable, String bookDescription) {
        return bookRepository.findBooksByBookDesc(pageable, bookDescription);
    }

    /*
     * book의 정보를 수정
     * @Param : BookRequestDto
     */
    @Override
    @Transactional
    public BookResponseDto modifyBook(BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(bookRequestDto.getBookIsbn()).orElseThrow(BookNotFoundException::new);
        bookCategoryRepository.saveAll(book.getCategories());
        bookTagRespository.saveAll(book.getTags());
        Book reqeustBook = new Book(book.getBookIsbn(),
                bookRequestDto.getBookTitle(),
                bookRequestDto.getBookDescription(),
                book.getBookPublisher(),
                bookRequestDto.getPublishedAt(),
                bookRequestDto.getBookFixedPrice(),
                bookRequestDto.getBookSalePrice(),
                bookRequestDto.isBookIsPacking(),
                bookRequestDto.getBookViews(),
                bookRequestDto.getBookStatus(),
                bookRequestDto.getBookQuantity(),
                bookRequestDto.getBookImage(),
                book.getCategories(),
                book.getTags(),
                bookRequestDto.getAuthor(),
                bookRequestDto.getLikes());
        Book modifyBook = bookRepository.save(reqeustBook);
        return new BookResponseDto(modifyBook);
    }

    /*
     * book의 status(판매중(0), 수량부족(1), 판매종료(2), 삭제된 도서(3))를 변경
     * @Param : bookIsbn, bookStatus
     */
    @Override
    @Transactional
    public BookResponseDto modifyBookStatus(String bookIsbn, int bookStatus) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);
        bookRepository.save(new Book(bookIsbn, book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), bookStatus, book.getBookQuantity(), book.getBookImage(),
                book.getCategories(), book.getTags(), book.getAuthor(), book.getLikes()));

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(), book.getBookPublishedAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(),
                book.getTags().stream()
                        .map(BookTag::getTag)
                        .map(TagResponseDto::new)
                        .collect(Collectors.toList()),
                book.getAuthor(), book.getLikes());
    }
}
