package com.nhnacademy.shop.bookcategory.repository;

import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategory.Pk> {
    List<BookCategory> findById(Long categoryId);
}
