package com.nhnacademy.shop.repository;

import com.nhnacademy.shop.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
