package com.nhnacademy.shop.author.repository;

import com.nhnacademy.shop.author.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 저자(Author) repository.
 *
 * @author : 박동희
 * @date : 2024-03-20
 *
 **/
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Page<Author> findAuthorsByAuthorName(String authorName, Pageable pageable);
    Author findByAuthorId(Long authorId);
}
