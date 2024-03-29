package com.nhnacademy.shop.author.repository;

import com.nhnacademy.shop.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 저자(Author) repository.
 *
 * @author : 박동희
 * @date : 2024-03-20
 *
 **/
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAuthorsByAuthorName(String authorName);
}
