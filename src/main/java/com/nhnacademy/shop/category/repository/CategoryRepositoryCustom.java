package com.nhnacademy.shop.category.repository;

import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CategoryRepositoryCustom {
    List<ParentCategoryInfoResponseDto> getParentCategories();
    List<ParentCategoryResponseDto> getParentCategoriesWithChildCategories();
    ParentCategoryResponseDto getParentCategory(Long categoryId);

}
