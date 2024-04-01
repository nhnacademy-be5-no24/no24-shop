package com.nhnacademy.shop.book.controller;

import java.util.List;
import java.util.Objects;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.service.BookService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



/**
 * 도서관리 RestController
 *
 * @author : 이재원
 * @date : 2024-03-31
 */
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


     /**
     *  새로운 book을 생성하는 method
     *
     * @param BookCreateRequestDto : 새로운 Book을 생성하기 위한 DTO 
     * @throws ResponseStatusException 해당 DTO를 통해 이미 존재하는 경우 응답코드 208 ALREADY_REPORTED 을 return
     * @return 성공했을 때 응답코드 201 CREATED return
     */
    @PostMapping("/books")
    public ResponseEntity<BookResponseDto> createBook(BookCreateRequestDto request){
        BookResponseDto book = bookService.createBook(request);
        if(!Objects.isNull(book)){
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "GET : This book is Already Exists");
        }  

        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /**
     * book isbn을 통해서 book을 삭제된 도서로 만드는 method
     *
     * @param String BookIsbn : book 삭제를 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @DeleteMapping("/books/{bookIsbn}")
    public ResponseEntity<BookResponseDto> deleteBook(@PathVariable String bookIsbn){
        BookResponseDto book = bookService.deleteBook(bookIsbn);
        if(Objects.isNull(book)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     *  저장된 book의 list를 가져오는 Method
     *
     * @param null
     * @throws ResponseStatusException findAllBooks()를 통해 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDto>> getAllBooks(){
        List<BookResponseDto> books = bookService.findAllBooks();
        if(Objects.isNull(books)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }        
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * book isbn을 통해서 book을 기존의 book 정보를 가져오는 method
     *
     * @param String BookIsbn : book을 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/books/{bookIsbn}")
    public ResponseEntity<BookResponseDto> getByIsbn(@PathVariable String bookIsbn){
        BookResponseDto book = bookService.findByIsbn(bookIsbn);
        if(Objects.isNull(book)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return null;
    }

    /**
     * book category을 통해서 book의 정보를 가져오는 method
     *
     * @param Long categoryId : book을 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/shop/{categoryId}")
    public ResponseEntity<List<BookResponseDto>> getBookByCategory(@PathVariable Long categoryId){
        List<BookResponseDto> books = bookService.findByCategoryId(categoryId);
        if(Objects.isNull(books)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }   

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * book name을 통해서 book의 정보를 가져오는 method
     *
     * @param String bookName : book을 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/books/{bookName}")
    public ResponseEntity<List<BookResponseDto>> getBookByName(@PathVariable String bookName){
        List<BookResponseDto> books = bookService.findByName(bookName);
        if(Objects.isNull(books)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

     /**
     * author를 통해서 book의 정보를 가져오는 method
     *
     * @param Long authorId : book을 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("books/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBookByAuthor(@PathVariable Long authorId){
        List<BookResponseDto> books = bookService.findByAuthor(authorId);
        if(Objects.isNull(books)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * book description을 통해서 book의 정보를 가져오는 method
     *
     * @param String bookDescription : book을 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/books/{bookDescription}")
    public ResponseEntity<List<BookResponseDto>> getByDesc(@PathVariable String bookDescription){
        List<BookResponseDto> books = bookService.findByDescription(bookDescription);
        if(Objects.isNull(books)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Tag을 통해서 book의 정보를 가져오는 method
     *
     * @param Long tagId : book을 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/books/{tagId}")
    public ResponseEntity<List<BookResponseDto>> getByTag(@PathVariable Long tagId){
        List<BookResponseDto> books = bookService.findByTag(tagId);
        if(Objects.isNull(books)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * book 의 정보를 수정하는 method
     *
     * @param BookRequestDto : 수정된 book의 정보
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @PutMapping
    public ResponseEntity<BookResponseDto> modifyBook(BookRequestDto bookRequestDto){
        BookResponseDto book = bookService.modifyBook(bookRequestDto);
        if(Objects.isNull(book)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * book ISBN을 통해서 book의 status를 변경하는 method
     *
     * @param String bookIsbn : book 을 찾기위한 isbn,
     *  int bookStatus : 변경될 book의 status
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @PutMapping
    public ResponseEntity<BookResponseDto> modifyBookStatus(String bookIsbn, int bookStatus){
        BookResponseDto book = bookService.modifyBookStatus(bookIsbn, bookStatus);
        if(Objects.isNull(book)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Book NOT FOUND");
        }  

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }




    
}
