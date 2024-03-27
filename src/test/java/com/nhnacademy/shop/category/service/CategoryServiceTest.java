package com.nhnacademy.shop.category.service;

import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.request.CategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.category.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {


    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    private Category category;
    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
        category = new Category(null, "Hello", null);
    }

    @Test
    void getCategoryTest() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findById(anyLong())).thenReturn(
                Optional.of(category));

        categoryService.getCategory(anyLong());

        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    void getCategoryTest_NotFound() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(null);

        assertThatThrownBy(() -> categoryService.getCategory(1L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");

    }

    // TODO test case parent child return 부분 cover 필요할 수도
    @Test
    void getCategoryByCategoryNameTest_Parent() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        categoryService.getCategoryByCategoryName(anyString());

        verify(categoryRepository, times(1)).findByCategoryName(anyString());
    }

    @Test
    void getCategoryByCategoryNameTest_Child() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);
        ReflectionTestUtils.setField(categoryResponseDto, "parentCategoryId", 1L);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        categoryService.getCategoryByCategoryName(anyString());

        verify(categoryRepository, times(1)).findByCategoryName(anyString());
    }

    @Test
    void getCategoryByCategoryNameTest_NotFound() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);

        assertThatThrownBy(() -> categoryService.getCategoryByCategoryName("hello"))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    void createCategoryTest() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);

        categoryService.createCategory(new CategoryRequestDto());

        verify(categoryRepository, times(1)).findByCategoryName(null);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void createCategoryTest_AlreadyExists() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setCategoryName(category.getCategoryName());

        assertThatThrownBy(() -> categoryService.createCategory(dto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining("이미 존재하는 카테고리입니다.");
    }

    @Test
    void modifyCategoryTest() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);

        categoryService.modifyCategory(new ModifyCategoryRequestDto());

        verify(categoryRepository, times(1)).findByCategoryName(null);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void modifyCategoryTest_AlreadyExists() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        ModifyCategoryRequestDto dto = new ModifyCategoryRequestDto();
        dto.setCategoryName(category.getCategoryName());

        assertThatThrownBy(() -> categoryService.modifyCategory(dto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining("이미 존재하는 카테고리입니다.");
    }

    @Test
    void modifyParentCategoryTest() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);

        ModifyCategoryRequestDto modifyCategoryRequestDto = new ModifyCategoryRequestDto();
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", categoryName);

        categoryService.modifyParentCategory(modifyCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(categoryName);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void modifyParentCategoryTest_AlreadyExists() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        ModifyCategoryRequestDto dto = new ModifyCategoryRequestDto();
        dto.setCategoryName(category.getCategoryName());

        assertThatThrownBy(() -> categoryService.modifyParentCategory(dto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining("이미 존재하는 카테고리입니다.");
    }


    @Test
    void deleteCategoryTest() {
        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void getParentWithChildCategoriesTest() {
        String categoryName = "Hello";

        ParentCategoryResponseDto categoryResponseDto = new ParentCategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        List<ParentCategoryResponseDto> dtoList = new ArrayList<>();
        dtoList.add(categoryResponseDto);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getParentCategoriesWithChildCategories()).thenReturn(dtoList);

        categoryService.getParentWithChildCategories();

        verify(categoryRepository, times(1)).getParentCategoriesWithChildCategories();
    }

    @Test
    void getParentWithChildCategoriesTest_NotFound() {
        String categoryName = "Hello";

        ParentCategoryResponseDto categoryResponseDto = new ParentCategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        List<ParentCategoryResponseDto> dtoList = new ArrayList<>();
        dtoList.add(categoryResponseDto);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getParentCategoriesWithChildCategories()).thenReturn(null);


        assertThatThrownBy(() -> categoryService.getParentWithChildCategories())
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    void getParentCategoriesTest() {
        String categoryName = "Hello";

        ParentCategoryInfoResponseDto categoryResponseDto = new ParentCategoryInfoResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        List<ParentCategoryInfoResponseDto> dtoList = new ArrayList<>();
        dtoList.add(categoryResponseDto);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getParentCategories()).thenReturn(dtoList);

        categoryService.getParentCategories();

        verify(categoryRepository, times(1)).getParentCategories();
    }


    @Test
    void getParentCategoriesTest_NotFound() {
        String categoryName = "Hello";

        ParentCategoryInfoResponseDto categoryResponseDto = new ParentCategoryInfoResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        List<ParentCategoryInfoResponseDto> dtoList = new ArrayList<>();
        dtoList.add(categoryResponseDto);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getParentCategories()).thenReturn(null);

        assertThatThrownBy(() -> categoryService.getParentCategories())
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    void getParentWithChildCategoryByParentCategoryIdTest() {
        String categoryName = "Hello";

        ParentCategoryResponseDto categoryResponseDto = new ParentCategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getParentCategory(1L)).thenReturn(categoryResponseDto);

        categoryService.getParentWithChildCategoryByParentCategoryId(1L);

        verify(categoryRepository, times(1)).getParentCategory(1L);
    }

    @Test
    void getParentWithChildCategoryByParentCategoryIdTest_NotFound() {
        String categoryName = "Hello";

        ParentCategoryResponseDto categoryResponseDto = new ParentCategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getParentCategory(1L)).thenReturn(null);

        assertThatThrownBy(() -> categoryService.getParentWithChildCategoryByParentCategoryId(1L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

}
