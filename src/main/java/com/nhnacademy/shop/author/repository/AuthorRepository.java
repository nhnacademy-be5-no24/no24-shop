package com.nhnacademy.shop.author.repository;

import com.nhnacademy.shop.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author : 박동희
 * @Date : 20/03/2024
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAuthorsByAuthorName(String authorName);
}
