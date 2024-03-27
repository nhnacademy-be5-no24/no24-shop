package com.nhnacademy.shop.category.repository;

import com.nhnacademy.shop.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    Category findByCategoryName(String categoryName);
}
