package com.nhnacademy.shop.book.repository;

import com.nhnacademy.shop.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 도서관리 repository
 *
 * @author : 이재원
 * @date : 2024-03-27
 */
public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> getAllBooks();

    List<Book> findByName(String bookName);

    Book findByBookIsbn(String bookIsbn);

    List<Book> findByBookDesc(String bookDesc);

}
