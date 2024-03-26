package com.nhnacademy.shop.book_author.service;

import com.nhnacademy.shop.book_author.domain.BookAuthor;
import com.nhnacademy.shop.book_author.dto.BookAuthorRequestDto;
import com.nhnacademy.shop.book_author.dto.BookAuthorResponseDto;

import java.util.List;

public interface BookAuthorService {
    /*
    한 도서에 저자 여려명 (1,1) (1,2) (1,3)
    한 저자가 많은 책 (1,1) (2,1) (3,1)
    필요한 method :
        조회 :
            저자 이름으로 조회하면 저자가 쓴 list 나오도록
            도서 id롴 조회 도서를 쓴 저자들 list 나오도록 -> book에서 해야할건디..
        등록 :
            도서 id랑 저자 list랑 엮기 -> book에서 해야할건디..
        수정 :
            도서에 대한 저자 수정 -> 이것도 book에서 해야할건디..
        삭제 :
            연관관계 삭제
     */

    List<BookAuthorResponseDto> getBookAuthorByBookIsbn(String bookIsbn);
    List<BookAuthorResponseDto> getBookAuthorByAuthorId(Long authorId);
    List<BookAuthorResponseDto> getBookAuthorByAuthorName(String authorName);
    BookAuthorResponseDto saveBookAuthor(BookAuthorRequestDto bookAuthorRequestDto);
    void deleteBookAuthor(String bookIsbn, Long authorId);

}
