package com.nhnacademy.shop.book.service;

import com.nhnacademy.shop.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.shop.book.dto.request.BookRequestDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * TagRepository 에 접근하기 위한 service interface
 *
 * @Author : 이재원
 * @Date : 28/03/2024
 *
 */
@Service
public interface BookService {

    BookResponseDto createBook(BookCreateRequestDto request);

    BookResponseDto deleteBook(String bookIsbn);

    List<BookResponseDto> findAllBooks();

    List<BookResponseDto> findByCategoryId(Long categoryId);

    List<BookResponseDto> findByName(String bookName);

    List<BookResponseDto> findByAuthor(Long authorId);

    BookResponseDto findByIsbn(String bookIsbn);

    List<BookResponseDto> findByDescription(String bookDescription);

    List<BookResponseDto> findByTag(Long tagId);

    BookResponseDto findIndividualBook(String bookIsbn);

    BookResponseDto modifyBook(BookRequestDto bookRequestDto);

    BookResponseDto modifyBookStatus(String bookIsbn, int bookStatus);



}