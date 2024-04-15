package com.nhnacademy.shop.book.repository;

import com.nhnacademy.shop.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 도서관리 repository
 *
 * @author : 이재원
 * @date : 2024-03-27
 */
public interface BookRepository extends JpaRepository<Book, String>, BookRepositoryCustom{

    Book findByBookIsbn(String bookIsbn);

}
