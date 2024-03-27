package com.nhnacademy.shop.category.service;

import com.nhnacademy.shop.category.dto.request.CategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* 카테고리 서비스입니다.
*
* @author : 강병구
*
 */
@Service
public interface CategoryService {

    CategoryResponseDto getCategory(Long categoryId);

    CategoryResponseDto getCategoryByCategoryName(String categoryName);

    void createCategory(CategoryRequestDto categoryRequestDto);

    void modifyCategory(ModifyCategoryRequestDto modifyCategoryRequestDto);

    void modifyParentCategory(ModifyCategoryRequestDto modifyCategoryRequestDto);

    void deleteCategory(Long categoryId);

    List<ParentCategoryResponseDto> getParentWithChildCategories();

    List<ParentCategoryInfoResponseDto> getParentCategories();

    ParentCategoryResponseDto getParentWithChildCategoryByParentCategoryId(Long categoryId);
}
