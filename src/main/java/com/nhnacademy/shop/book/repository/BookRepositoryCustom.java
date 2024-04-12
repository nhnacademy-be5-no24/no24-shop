package com.nhnacademy.shop.book.repository;

import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 도서관리 repository에서 query dsl을 사용하기 위한 custom repository
 *
 * @author : 이재원
 * @date : 2024-04-11
 */
@NoRepositoryBean
public interface BookRepositoryCustom{

    Page<BookResponseDto> getAllBooks(Pageable pageable);
    Page<BookResponseDto> getBooksByBookTitle(Pageable pageable, String bookTitle);

    Page<BookResponseDto> getBooksByBookDesc(Pageable pageable, String desc);

    Page<BookResponseDto> getBooksByAuthor(Pageable pageable, Long authorId);


}
