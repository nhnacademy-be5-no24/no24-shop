package com.nhnacademy.shop.category.repository;

import com.nhnacademy.shop.category.dto.response.CategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
/**
 * 카테고리 QueryDSL을 사용하기 위한 interface 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
@NoRepositoryBean
public interface CategoryRepositoryCustom {

    /**
     * 카테고리 기본 정보 조회.
     *
     * @return 최상위 카테고리 반환.
     */
    List<CategoryInfoResponseDto> getCategoriesInfo();

    /**
     * 상위 카테고리 조회 및 해당 하위 카테고리 조회.
     *
     * @return 최상위 카테고리 및 해당 하위 카테고리 리스트 반환.
     */
    List<ParentCategoryResponseDto> getParentCategoriesWithChildCategories();

    /**
     * 최상위 카테고리 조회 단건 조회
     *
     * @return 최상위 카테고리 반환.
     */
    ParentCategoryResponseDto getParentCategory(Long categoryId);

}
