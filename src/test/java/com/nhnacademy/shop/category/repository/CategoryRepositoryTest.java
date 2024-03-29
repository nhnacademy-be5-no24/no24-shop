package com.nhnacademy.shop.category.repository;

import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Category Repository 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles(value = "dev")
@Transactional
@WebAppConfiguration
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    @DisplayName(value = "카테고리 기본 정보 목록 조회")
    void testGetCategoriesInfo() {
        categoryRepository.deleteAll();

        Category category = Category.builder()
                .categoryName("Hello")
                .build();

        categoryRepository.save(category);

        Category category1 = Category.builder()
                .categoryName("World")
                .build();;

        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .categoryName("Bye")
                .parentCategory(category)
                .build();;

        categoryRepository.save(category2);

        assertThat(categoryRepository.getCategoriesInfo()).hasSize(3);
    }

    @Test
    @Order(2)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 목록 조회")
    void testGetParentCategoriesWithChildCategories() {

        Category category = Category.builder()
                .categoryName("Hello")
                .build();

        categoryRepository.save(category);

        Category category1 = Category.builder()
                .categoryName("World")
                .build();;

        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .categoryName("Bye")
                .parentCategory(category)
                .build();;

        categoryRepository.save(category2);

        assertThat(categoryRepository.getParentCategoriesWithChildCategories()).hasSize(2);
    }

    @Test
    @Order(2)
    @DisplayName(value = "상위 카테고리 단건 조회")
    void testGetParentCategory() {
        categoryRepository.deleteAll();

        Category category = Category.builder()
                .categoryName("Hello")
                .build();

        categoryRepository.save(category);

        Category category1 = Category.builder()
                .categoryName("World")
                .build();;

        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .categoryName("Bye")
                .parentCategory(category)
                .build();;

        categoryRepository.save(category2);

        ParentCategoryResponseDto dto = new ParentCategoryResponseDto();
        dto.setCategoryId(2L);
        dto.setCategoryName(category1.getCategoryName());
        dto.setChildCategories(new ArrayList<>());
        assertThat(categoryRepository.getParentCategory(2L)).isEqualTo(dto);

    }
}
