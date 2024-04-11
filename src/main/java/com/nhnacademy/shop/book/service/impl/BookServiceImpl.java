package com.nhnacademy.shop.book.service.impl;

import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.exception.BookAlreadyExistsException;
import com.nhnacademy.shop.book.exception.BookIsDeletedException;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.book.service.BookService;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.tag.domain.Tag;
import com.nhnacademy.shop.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 도서관리 Service 구현체
 *
 * @author : 이재원
 * @date : 2024-03-30
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;


    /*
     * book 생성
     * @Param : request
     */
    @Override
    public BookResponseDto createBook(BookCreateRequestDto request){
        if(Objects.nonNull(bookRepository.findByBookIsbn(request.getBookIsbn()))){
            throw new BookAlreadyExistsException();
        }
        Book book = Book.builder()
                .bookIsbn(request.getBookIsbn())
                .bookTitle(request.getBookTitle())
                .bookDesc(request.getBookDescription())
                .bookPublisher(request.getBookPublisher())
                .bookPublisherAt(request.getPublishedAt())
                .bookFixedPrice(request.getBookFixedPrice())
                .bookSalePrice(request.getBookSalePrice())
                .bookIsPacking(request.isBookIsPacking())
                .bookViews(request.getBookViews())
                .bookStatus(request.getBookStatus())
                .bookQuantity(request.getBookQuantity())
                .bookImage(request.getBookImage())
                .tags(request.getTags())
                .categories(request.getCategories())
                .author(request.getAuthor())
                .likes(0L).build();

        bookRepository.save(new Book(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(), book.getBookImage(),
                book.getCategories(), book.getTags(), book.getAuthor() ,book.getLikes()));

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(), book.getBookImage(),
                book.getTags(), book.getAuthor(), book.getCategories(), book.getLikes());
    }

    /*
     * book 삭제
     * @Param : bookIsbn
     */
    @Override
    public BookResponseDto deleteBook(String bookIsbn){
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);


        bookRepository.save(new Book(bookIsbn, book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), 3, book.getBookQuantity(), book.getBookImage(),
                null, null, null, book.getLikes()));

        return  new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(),book.getTags(),book.getAuthor(),book.getCategories() ,book.getLikes());
    }

    /*
     * 전체의 book 정보를 조회
     * @Param : pageable
     */
    @Override
    public Page<BookResponseDto> findAllBook(Pageable pageable) {
        Page<BookResponseDto> response = bookRepository.getAllBooks(pageable);

        if(response.getContent().isEmpty() || response.getTotalElements() == 0){
            throw new BookNotFoundException();
        }

        return response;
    }


    /*
     * category에 일치하는 book list 탐색
     * @Param : pageable, categoryId
     */
    @Override
    public Page<BookResponseDto> findByCategoryId(Pageable pageable,Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: "));

        Page<Book> books = bookRepository.findByCategoriesContaining(pageable, category);

        List<BookResponseDto> bookResponseDtoList = books.getContent().stream()
                .map(this::findBookByCategories)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtoList, pageable, books.getTotalElements());
    }

    private BookResponseDto findBookByCategories(Book book){

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher(), book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthor(), book.getCategories(), null);
    }
    /*
     * book tag에 일치하는 book list 탐색
     * @Param : pageable, tagId
     */
    @Override
    public Page<BookResponseDto> findByTag(Pageable pageable, Long tagId) {
        Tag tag = tagRepository.findByTagId(tagId);

        Page<Book> books = bookRepository.findByTagsContaining(pageable, tag);

        List<BookResponseDto> bookResponseDtoList = books.getContent().stream()
                .map(this::findBookByTags)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtoList, pageable, books.getTotalElements());
    }

    private BookResponseDto findBookByTags(Book book){

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthor(), book.getCategories(), null);
    }
    /*
     * book name에 일치하는 book list 탐색
     * @Param : pageable, bookName
     */
    @Override
    public Page<BookResponseDto> findByTitle(Pageable pageable, String bookTitle) {
        return bookRepository.getBooksByBookTitle(pageable, bookTitle);
    }

    /*
     * author에 일치하는 book list 탐색
     * @Param : pageable, authorId
     */
    @Override
    public Page<BookResponseDto> findByAuthor(Pageable pageable, Long authorId) {
        Page<BookResponseDto> response = bookRepository.getBooksByAuthor(pageable, authorId);

        if(response.getContent().isEmpty() || response.getTotalElements() == 0){
            throw new BookNotFoundException();
        }

        return response;
    }

    /*
     * book ISBN에 일치하는 book list 탐색
     * @Param : bookIsbn
     */

    @Override
    public BookResponseDto findByIsbn(String bookIsbn) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);



        if(book.getBookStatus()==3){
            throw new BookIsDeletedException();
        }

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthor(), book.getCategories(), null);
    }
    /*
     * book description에 일치하는 book list 탐색
     * @Param : bookDescription
     */
    @Override
    public Page<BookResponseDto> findByDescription(Pageable pageable, String bookDescription) {
        return bookRepository.getBooksByBookDesc(pageable, bookDescription);
    }

    /*
     * book의 정보를 수정
     * @Param : BookRequestDto
     */
    @Override
    public BookResponseDto modifyBook(BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(bookRequestDto.getBookIsbn()).orElseThrow(BookNotFoundException::new);

        bookRepository.save(new Book(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews()
                , book.getBookStatus(), book.getBookQuantity(), book.getBookImage(), book.getCategories(), book.getTags(), book.getAuthor(), book.getLikes()));

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthor(), book.getCategories(),null);
    }

    /*
     * book의 status(판매중(0), 수량부족(1), 판매종료(2), 삭제된 도서(3))를 변경
     * @Param : bookIsbn, bookStatus
     */
    @Override
    public BookResponseDto modifyBookStatus(String bookIsbn, int bookStatus) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);

        bookRepository.save(new Book(bookIsbn, book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), bookStatus, book.getBookQuantity(), book.getBookImage(),
                book.getCategories(),book.getTags(), book.getAuthor(), book.getLikes()));


        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), bookStatus, book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthor(), book.getCategories(),null);
    }
}
