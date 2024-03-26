package com.nhnacademy.shop.book.repository;

import com.nhnacademy.shop.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
