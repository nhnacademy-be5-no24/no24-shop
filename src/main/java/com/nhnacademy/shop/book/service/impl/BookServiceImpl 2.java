package com.nhnacademy.shop.book.service.impl;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.book.service.BookService;
import com.nhnacademy.shop.book_author.domain.BookAuthor;
import com.nhnacademy.shop.book_author.repository.BookAuthorRepository;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.shop.book_tag.domain.BookTag;
import com.nhnacademy.shop.book_tag.repository.BookTagRespository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 도서관리 Service class
 *
 * @author : 이재원
 * @date : 2024-03-30
 */
@Service("bookService")
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookTagRespository bookTagRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookAuthorRepository bookAuthorRepository;


    /*
     * book 생성
     * @Param : BookCreateReqeustDto request
     */
    @Override
    @Transactional
    public BookResponseDto createBook(BookCreateRequestDto request){
        bookRepository.save(new Book(request.getBookIsbn(), request.getBookTitle(), request.getBookDescription(), request.getBookPublisher(),
        request.getPublishedAt(), request.getBookFixedPrice(), request.getBookSalePrice(), request.isBookIsPacking(), request.getBookViews(), request.getBookStatus(), request.getBookQuantity(), request.getBookImage(),
        request.getTags(), request.getCategories(), request.getAuthors()));


        return new BookResponseDto(request.getBookIsbn(), request.getBookTitle(), request.getBookDescription(), request.getBookPublisher(), 
        request.getPublishedAt(), request.getBookFixedPrice(), request.getBookSalePrice(), request.isBookIsPacking(), request.getBookViews(), request.getBookStatus(), request.getBookQuantity(), request.getBookImage(), 
        request.getTags(), request.getAuthors(), request.getCategories(), request.getLikes());
    }

    /*
     * book 삭제
     * @Param : String bookIsbn
     */
    @Override
    @Transactional
    public BookResponseDto deleteBook(String bookIsbn){
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);

        bookRepository.save(new Book(bookIsbn, book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
        book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), 3, book.getBookQuantity(), book.getBookImage(),
       book.getTags(), book.getCategories(), book.getAuthors()));

        return  new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher() ,book.getBookPublisherAt(),
        book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
        book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null);
        
    }

     /*
     * 전체의 book 정보를 조회
     * @Param : null
     */
    //TODO : 전체적인 book 정보 반환 필요
    @Override
    @Transactional
    public List<BookResponseDto> findAllBooks() {
        List<BookResponseDto> responses = new ArrayList<>();
        List<Book> books = bookRepository.findAll();

        for(Book book : books){
            responses.add(findBookByNames(book));
        }

        return responses;
    }

    /*
    * category에 일치하는 book list 탐색
    * @Param : Long categoryId
     */
    @Override
    @Transactional
    public List<BookResponseDto> findByCategoryId(Long categoryId) {
        List<BookCategory> categories = bookCategoryRepository.findById(categoryId);
        List<BookResponseDto> responses = new ArrayList<>();

        for(BookCategory bookCategory : categories){
            responses.add(findBookByCategories(bookCategory));
        }

        return responses;
    }

    private BookResponseDto findBookByCategories(BookCategory bookCategory){
        Book book = bookRepository.findById(bookCategory.getPk().getBookIsbn()).orElseThrow(BookNotFoundException::new);

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher(), book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null);
    }

    /*
     * book name에 일치하는 book list 탐색
     * @Param : String bookName
     */
    @Override
    @Transactional
    public List<BookResponseDto> findByName(String bookName) {
        List<Book> books = bookRepository.findByName(bookName);
        List<BookResponseDto> responses= new ArrayList<>();

        for(Book book : books){
            responses.add(findBookByNames(book));
        }

        return responses;
    }

    private BookResponseDto findBookByNames(Book book){
        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null);
    }

    /*
    * author에 일치하는 book list 탐색
    * @Param : Long authorId
     */
    @Override
    @Transactional
    public List<BookResponseDto> findByAuthor(Long authorId) {
        List<BookAuthor> books = bookAuthorRepository.findByPkAuthorId(authorId);
        List<BookResponseDto> responses = new ArrayList<>();

        for(BookAuthor bookAuthor : books){
            responses.add(findBookByAuthors(bookAuthor));
        }


        return responses;
    }

    private BookResponseDto findBookByAuthors(BookAuthor bookAuthor){
        Book book = bookRepository.findById(bookAuthor.getPk().getBookIsbn()).orElseThrow(BookNotFoundException::new);

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
        book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
        book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null);
    }
    
    /*
     * book ISBN에 일치하는 book list 탐색
     * @Param : String bookIsbn
     */

    @Override
    @Transactional
    public BookResponseDto findByIsbn(String bookIsbn) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);
        

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
        book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
        book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null);
    }
    /*
     * book description에 일치하는 book list 탐색
     * @Param : String bookDescription
     */
    @Override
    @Transactional
    public List<BookResponseDto> findByDescription(String bookDescription) {
        List<Book> books = bookRepository.findByBookDesc(bookDescription);
        List<BookResponseDto> responses = new ArrayList<>();

        for(Book book : books){
            responses.add(findBookByNames(book));
        }

        return responses;
    }

     /*
     * book tag에 일치하는 book list 탐색
     * @Param : Long tagId
     */
    @Override
    @Transactional
    public List<BookResponseDto> findByTag(Long tagId) {
        List<BookTag> books = bookTagRepository.findByTagId(tagId);
        List<BookResponseDto> responses = new ArrayList<>();

        for(BookTag bookTag : books){
            responses.add(findBookByTags(bookTag));
        }

        return responses;
    }

    private BookResponseDto findBookByTags(BookTag bookTag){
        Book book = bookRepository.findById(bookTag.getPk().getBookIsbn()).orElseThrow(BookNotFoundException::new);


        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
        book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
        book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null);
    }



    /*
     * book ISBN을 통해서 정보를 받아오는데 다른 method를 구현해야하는지?
     * 조회수를 올리기위한 method는 각각의 method에 code를 추가해도 될 것 같음.
     */
    @Override
    public BookResponseDto findIndividualBook(String bookIsbn) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);

        List<Tag> tags = new ArrayList<>();
//        List<BookTag> oneOfTag = book.getTags();
        Tag tag = (Tag) bookTagRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);


        List<Author> authors = new ArrayList<>();
        List<Category> categories= new ArrayList<>();

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
                book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
                book.getBookImage(), null, null, null, null);
    }

     /*
     * book의 정보를 수정 
     * @Param : BookRequestDto
     */
    @Override
    @Transactional
    public BookResponseDto modifyBook(BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(bookRequestDto.getBookIsbn()).orElseThrow(BookNotFoundException::new);

        bookRepository.save(new Book(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
         book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(), book.getBookImage(),
        book.getTags(), book.getCategories(), book.getAuthors()));

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
        book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
        book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(),null);
    }

     /*
     * book의 status(판매중, 수량부족, 판매종료, 삭제된 도서)를 변경
     * @Param : String bookIsbn, int bookStatus
     */
    @Override
    @Transactional
    public BookResponseDto modifyBookStatus(String bookIsbn, int bookStatus) {
        Book book = bookRepository.findById(bookIsbn).orElseThrow(BookNotFoundException::new);
        
        bookRepository.save(new Book(bookIsbn, book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
         book.getBookPublisherAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), bookStatus, book.getBookQuantity(), book.getBookImage(),
        book.getTags(), book.getCategories(), book.getAuthors()));
        

        return new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher() ,book.getBookPublisherAt(),
        book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), bookStatus, book.getBookQuantity(),
        book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(),null);
    }
}
