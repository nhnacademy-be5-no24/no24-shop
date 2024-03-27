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
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;
    private Category category;
    private Category childCategory;
    private CategoryRequestDto categoryRequestDto;
    private CategoryRequestDto childCategoryRequestDto;
    private ModifyCategoryRequestDto modifyCategoryRequestDto;
    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
        childCategory = new Category(2L, "World", category);
        childCategoryRequestDto = new CategoryRequestDto("World", 1L);
        category = new Category(1L, "Hello", null);
        categoryRequestDto = new CategoryRequestDto("Hello", null);
        modifyCategoryRequestDto = new ModifyCategoryRequestDto(1L, "Hello", null);
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
        categoryRepository.save(childCategory);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);
        ReflectionTestUtils.setField(categoryResponseDto, "parentCategoryId", 1L);

        when(categoryRepository.save(any())).thenReturn(childCategory);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(childCategory);

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
    void createCategoryTest_Parent() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);

        CategoryResponseDto dto = categoryService.createCategory(categoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(categoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
        assertThat(category.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(category.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(category.getParentCategory()).isNull();
    }

    @Test
    void createCategoryTest_Child() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(childCategory);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(childCategory);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryResponseDto dto = categoryService.createCategory(childCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(childCategoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(childCategory);
        assertThat(childCategory.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(childCategory.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(childCategory.getParentCategory()).isEqualTo(dto.getParentCategoryId());
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
    void modifyCategoryTest_Parent() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);

        CategoryResponseDto dto = categoryService.modifyCategory(modifyCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(modifyCategoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
        assertThat(category.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(category.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(category.getParentCategory()).isNull();
    }

    @Test
    void modifyCategoryTest_Child() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(childCategory);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(childCategory);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryResponseDto dto = categoryService.modifyCategory(modifyCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(modifyCategoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(childCategory);
        assertThat(childCategory.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(childCategory.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(childCategory.getParentCategory()).isEqualTo(dto.getParentCategoryId());
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

        ParentCategoryResponseDto dto = categoryService.modifyParentCategory(modifyCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(modifyCategoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
        assertThat(category.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(category.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(category.getParentCategory()).isNull();
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

    @Test
    void getParentCategory_NotFound() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setCategoryName(category.getCategoryName());

        assertThatThrownBy(() -> categoryService.getParentWithChildCategoryByParentCategoryId(1L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

}
