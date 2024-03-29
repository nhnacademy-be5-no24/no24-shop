package com.nhnacademy.shop.category.service;

import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.CategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.category.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * Category Service 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
class CategoryServiceTest {
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;
    private Category category;
    private Category childCategory;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private CreateCategoryRequestDto childCreateCategoryRequestDto;
    private ModifyCategoryRequestDto modifyCategoryRequestDto;
    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
        childCategory = new Category(2L, "World", category);
        childCreateCategoryRequestDto = new CreateCategoryRequestDto("World", 1L);
        category = new Category(1L, "Hello", null);
        createCategoryRequestDto = new CreateCategoryRequestDto("Hello", null);
        modifyCategoryRequestDto = new ModifyCategoryRequestDto(1L, "Hello", null);
    }

    @Test
    @Order(1)
    @DisplayName(value = "카테고리 단건 조회 성공")
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
    @Order(2)
    @DisplayName(value = "카테고리 단건 조회 실패 - CategoryNotFoundException")
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
    @Order(3)
    @DisplayName(value = "상위 카테고리 단건 조회 성공")
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
    @Order(4)
    @DisplayName(value = "하위 카테고리 단건 조회 성공")
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
    @Order(5)
    @DisplayName(value = "카테고리 단건 조회 실패 - CategoryNotFoundException")
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
    @Order(6)
    @DisplayName(value = "상위 카테고리 생성 성공")
    void createCategoryTest_Parent() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);

        CategoryResponseDto dto = categoryService.createCategory(createCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(createCategoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
        assertThat(category.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(category.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(category.getParentCategory()).isNull();
    }

    @Test
    @Order(7)
    @DisplayName(value = "하위 카테고리 생성 성공")
    void createCategoryTest_Child() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(childCategory);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(childCategory);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(null);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryResponseDto dto = categoryService.createCategory(childCreateCategoryRequestDto);

        verify(categoryRepository, times(1)).findByCategoryName(childCreateCategoryRequestDto.getCategoryName());
        verify(categoryRepository, times(1)).save(childCategory);
        assertThat(childCategory.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(childCategory.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(childCategory.getParentCategory()).isEqualTo(dto.getParentCategoryId());
    }

    @Test
    @Order(8)
    @DisplayName(value = "이미 존재하는 카테고리 생성 요청으로 인한 생성 실패 - CategoryAlreadyExistsException")
    void createCategoryTest_AlreadyExists() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setCategoryName(category.getCategoryName());

        assertThatThrownBy(() -> categoryService.createCategory(dto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining("이미 존재하는 카테고리입니다.");
    }

    @Test
    @Order(9)
    @DisplayName(value = "하위 카테고리 수정 성공")
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
    @Order(10)
    @DisplayName(value = "카테고리 수정 시 이미 존재하는 이름으로 인한 수정 실패 - CategoryAlreadyExistsException")
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
    @Order(11)
    @DisplayName(value = "상위 카테고리 수정 성공")
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
    @Order(12)
    @DisplayName(value = "상위 카테고리 수정 시 이미 존재하는 이름으로 인한 수정 실패 - CategoryAlreadyExistsException")
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
    @Order(13)
    @DisplayName(value = "카테고리 삭제 성공")
    void deleteCategoryTest() {
        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    @Order(14)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 목록 조회 성공")
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
    @Order(15)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 목록 조회 실패 - CategoryNotFoundException")
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
    @Order(16)
    @DisplayName(value = "카테고리 기본 정보 목록 조회 성공")
    void getCategoriesInfoTest() {
        String categoryName = "Hello";

        CategoryInfoResponseDto categoryResponseDto = new CategoryInfoResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        List<CategoryInfoResponseDto> dtoList = new ArrayList<>();
        dtoList.add(categoryResponseDto);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getCategoriesInfo()).thenReturn(dtoList);

        categoryService.getCategoriesInfo();

        verify(categoryRepository, times(1)).getCategoriesInfo();
    }


    @Test
    @Order(17)
    @DisplayName(value = "카테고리 기본 정보 목록 조회 실패 - CategoryNotFoundExceptiojn")
    void getCategoriesInfoTest_NotFound() {
        String categoryName = "Hello";

        CategoryInfoResponseDto categoryResponseDto = new CategoryInfoResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.getCategoriesInfo()).thenReturn(null);

        assertThatThrownBy(() -> categoryService.getCategoriesInfo())
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

    @Test
    @Order(18)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 단건 조회 성공")
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
    @Order(19)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 단건 조회 실패 - CategoryNotFoundException")
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
    @Order(20)
    @DisplayName(value = "Category Service 내부 메소드 상위 카테고리 정보 조회 실패")
    void getParentCategory_NotFound() {
        String categoryName = "Hello";

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryRepository.save(category);
        ReflectionTestUtils.setField(categoryResponseDto, "categoryName", categoryName);

        when(categoryRepository.save(any())).thenReturn(category);
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);

        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setCategoryName(category.getCategoryName());

        assertThatThrownBy(() -> categoryService.getParentWithChildCategoryByParentCategoryId(1L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("해당 카테고리를 찾을 수 없습니다.");
    }

}
