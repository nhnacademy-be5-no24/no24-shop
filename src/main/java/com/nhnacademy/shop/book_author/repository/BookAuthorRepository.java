package com.nhnacademy.shop.book_author.repository;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book_author.domain.BookAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthor.Pk> {
    Page<Book> findByAuthor(Pageable pageable, Author author);
}
