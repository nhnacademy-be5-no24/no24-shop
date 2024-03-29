package com.nhnacademy.shop.bookcategory.repository;

import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategory.Pk> {
}
