package com.nhnacademy.shop.book_author.repository;

import com.nhnacademy.shop.book_author.domain.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthor.Pk> {

    List<BookAuthor> findByPkBookIsbn(String bookIsbn);
    List<BookAuthor> findByPkAuthorId(Long authorId);
    void deleteByPkBookIsbnAndPkAuthorId(String bookIsbn, Long authorId);
}
