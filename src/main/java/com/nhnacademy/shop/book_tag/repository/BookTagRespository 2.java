package com.nhnacademy.shop.book_tag.repository;

import com.nhnacademy.shop.book_tag.domain.BookTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;


public interface BookTagRespository extends JpaRepository<BookTag, BookTag.Pk> {
    Optional<Object> findById(String bookIsbn);
    List<BookTag> findByTagId (Long tagId);
}
