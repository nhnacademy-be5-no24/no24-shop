package com.nhnacademy.shop.category.repository;

import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.response.CategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    Category child;
    Category parent;
    Pageable pageable;
    Integer pageSize;
    Integer offset;

    @BeforeEach
    void setUp() {
        parent = Category.builder()
                .categoryName("Programming")
                .parentCategory(null)
                .build();

        child = Category.builder()
                .categoryName("html")
                .parentCategory(parent)
                .build();
        pageSize = 0;
        offset = 10;
        pageable = PageRequest.of(pageSize, offset);

    }

    @Test
    @Order(1)
    @DisplayName(value = "전체 리뷰 조회")
    void testFindReviews() {

        parent = categoryRepository.save(parent);

        Page<CategoryResponseDto> dtoPage = categoryRepository.findCategories(pageable);
        List<CategoryResponseDto> dtoList = dtoPage.getContent();

        assertThat(dtoList).isNotEmpty();
        assertThat(dtoList.get(0).getCategoryId()).isEqualTo(parent.getCategoryId());
        assertThat(dtoList.get(0).getCategoryName()).isEqualTo(parent.getCategoryName());
        assertThat(dtoList.get(0).getParentCategory().getCategoryId()).isNull();
        assertThat(dtoList.get(0).getParentCategory().getCategoryName()).isNull();
    }

    @Test
    @Order(2)
    @DisplayName(value = "카테고리 기본 정보 목록 조회")
    void testGetCategoriesInfo() {
        parent = categoryRepository.save(parent);
        List<CategoryInfoResponseDto> dtoList = categoryRepository.findCategoriesInfo();

        assertThat(dtoList).isNotEmpty();
        assertThat(dtoList.get(0).getCategoryId()).isEqualTo(parent.getCategoryId());
        assertThat(dtoList.get(0).getCategoryName()).isEqualTo(parent.getCategoryName());
    }

    @Test
    @Order(3)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 목록 조회")
    void testGetParentCategoriesWithChildCategories() {
        parent = categoryRepository.save(parent);
        child = categoryRepository.save(child);

        List<ParentCategoryResponseDto> dtoList = categoryRepository.findParentCategoriesWithChildCategories();

        assertThat(dtoList).isNotEmpty();
        assertThat(dtoList.get(0).getCategoryId()).isEqualTo(parent.getCategoryId());
        assertThat(dtoList.get(0).getCategoryName()).isEqualTo(parent.getCategoryName());
        assertThat(dtoList.get(0).getChildCategories().get(0).getCategoryId()).isEqualTo(child.getCategoryId());
        assertThat(dtoList.get(0).getChildCategories().get(0).getCategoryName()).isEqualTo(child.getCategoryName());
    }
    @Test
    @Order(4)
    @DisplayName(value = "상위 카테고리 단건 조회")
    void testGetParentCategory() {
        parent = categoryRepository.save(parent);
        child = categoryRepository.save(child);

        ParentCategoryResponseDto dto = categoryRepository.findParentCategory(parent.getCategoryId());

        assertThat(dto).isNotNull();
        assertThat(dto.getCategoryId()).isEqualTo(parent.getCategoryId());
        assertThat(dto.getCategoryName()).isEqualTo(parent.getCategoryName());
        assertThat(dto.getChildCategories().get(0).getCategoryId()).isEqualTo(child.getCategoryId());
        assertThat(dto.getChildCategories().get(0).getCategoryName()).isEqualTo(child.getCategoryName());
    }
}
