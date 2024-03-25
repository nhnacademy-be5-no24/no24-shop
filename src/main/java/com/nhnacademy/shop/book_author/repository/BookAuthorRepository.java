package com.nhnacademy.shop.repository;

import com.nhnacademy.shop.domain.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthor.Pk> {
}
