package com.nhnacademy.shop.repository;

import com.nhnacademy.shop.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @Author : 박동희
 * @Date : 20/03/2024
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
