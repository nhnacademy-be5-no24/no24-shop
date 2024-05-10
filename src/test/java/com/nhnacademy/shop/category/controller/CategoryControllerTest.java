package com.nhnacademy.shop.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.CategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.service.CategoryService;
import com.nhnacademy.shop.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Category RestController 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
@WebMvcTest(CategoryController.class)
@Import(
        {RedisConfig.class}
)
public class CategoryControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    private ObjectMapper objectMapper = new ObjectMapper();
    CreateCategoryRequestDto createCategoryRequestDto;
    ModifyCategoryRequestDto modifyCategoryRequestDto;
    Category category;
    CategoryResponseDto categoryResponseDto;
    ParentCategoryResponseDto parentCategoryResponseDto;
    Page<CategoryResponseDto> categoryPage;
    Integer pageSize;
    Integer offset;
    Pageable pageable;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryController(categoryService)).build();
        createCategoryRequestDto = new CreateCategoryRequestDto("로맨스", null);
        modifyCategoryRequestDto = new ModifyCategoryRequestDto(1L, "판타지", null);
        category = new Category(1L, "판타지", null);
        categoryResponseDto = new CategoryResponseDto(1L, "판타지", null);
        parentCategoryResponseDto = new ParentCategoryResponseDto(1L, "판타지");
        pageSize = 0;
        offset = 10;
        pageable = PageRequest.of(pageSize, offset);
        categoryPage = new PageImpl<>(List.of(categoryResponseDto), pageable, 1);

    }

    @Test
    @Order(1)
    @DisplayName(value = "카테고리 생성 시 성공")
    void createCategoryTest_Success() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.createCategory(createCategoryRequestDto)).thenReturn(categoryResponseDto);
        try {
            mockMvc.perform(post("/shop/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createCategoryRequestDto)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName(value = "카테고리 생성 시 이름 중복으로 인한 CONFLICT 반환")
    void createCategoryTest_Conflict() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);
        doThrow(CategoryAlreadyExistsException.class).when(categoryService).createCategory(createCategoryRequestDto);
        try {
            mockMvc.perform(post("/shop/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequestDto)))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName(value = "카테고리 단건 조회 성공 (카테고리 아이디)")
    void getCategoryTest_Success() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.getCategory(anyLong())).thenReturn(new CategoryResponseDto());
        try {
            mockMvc.perform(get("/shop/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName(value = "카테고리 단건 조회 실패 (카테고리 아이디) - Not Found")
    void getCategoryTest_NotFound() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.getCategory(anyLong())).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    @DisplayName(value = "카테고리 단건 조회 성공 (카테고리 이름)")
    void getCategoryByNameTest_Success() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.getCategoryByCategoryName(anyString())).thenReturn(new CategoryResponseDto());
        try {
            mockMvc.perform(get("/shop/categories/name/hello")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName(value = "카테고리 단건 조회 실패 (카테고리 이름) - Not Found")
    void getCategoryByNameTest_NotFound() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.getCategoryByCategoryName(anyString())).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/categories/name/hello")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 목록 조회 성공")
    void getCategoriesTest_Success() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);

        List<ParentCategoryResponseDto> dtoList = new ArrayList<>();
        when(categoryService.getParentWithChildCategories()).thenReturn(dtoList);
        try {
            mockMvc.perform(get("/shop/categories")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 목록 조회 실패 - Not Found")
    void getCategoriesTest_NotFound() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);

        when(categoryService.getParentWithChildCategories()).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/categories")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(9)
    @DisplayName(value = "상위 카테고리 목록 조회 성공")
    void getParentCategoriesTest_Success() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);

        List<CategoryInfoResponseDto> dtoList = new ArrayList<>();
        when(categoryService.getCategoriesInfo()).thenReturn(dtoList);
        try {
            mockMvc.perform(get("/shop/categories/parents")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    @DisplayName(value = "상위 카테고리 목록 조회 실패 - Not Found")
    void getParentCategoriesTest_NotFound() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);

        when(categoryService.getCategoriesInfo()).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/categories/parents")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(11)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 단건 조회 성공")
    void getParentWithChildCategoryTest_Success() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);

        when(categoryService.getParentWithChildCategoryByParentCategoryId(anyLong())).thenReturn(new ParentCategoryResponseDto());
        try {
            mockMvc.perform(get("/shop/categories/parents/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(12)
    @DisplayName(value = "상위 카테고리 및 해당 하위 카테고리 단건 조회 실패 - Not Found")
    void getParentWithChildCategoryTest_NotFound() {
        ReflectionTestUtils.setField(createCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(createCategoryRequestDto, "parentCategoryId", null);

        when(categoryService.getParentWithChildCategoryByParentCategoryId(anyLong())).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/categories/parents/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(13)
    @DisplayName(value = "상위 카테고리 수정 성공")
    void modifyParentCategoryTest_Success() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.modifyParentCategory(modifyCategoryRequestDto)).thenReturn(parentCategoryResponseDto);
        try {
            mockMvc.perform(put("/shop/categories/parents")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(14)
    @DisplayName(value = "상위 카테고리 중복 이름으로 인한 수정 실패 - Conflict")
    void modifyParentCategoryTest_Conflict() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        doThrow(CategoryAlreadyExistsException.class).when(categoryService).modifyParentCategory(modifyCategoryRequestDto);
        try {
            mockMvc.perform(put("/shop/categories/parents")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(15)
    @DisplayName(value = "하위 카테고리 수정 성공")
    void modifyCategoryTest_Success() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        when(categoryService.modifyCategory(modifyCategoryRequestDto)).thenReturn(categoryResponseDto);
        try {
            mockMvc.perform(put("/shop/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(16)
    @DisplayName(value = "하위 카테고리 이름 중복으로 인한 수정 실패")
    void modifyCategoryTest_Conflict() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        doThrow(CategoryAlreadyExistsException.class).when(categoryService).modifyCategory(modifyCategoryRequestDto);
        try {
            mockMvc.perform(put("/shop/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(modifyCategoryRequestDto)))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(17)
    @DisplayName(value = "카테고리 삭제 성공")
    void deleteCategoryTest_Success() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        doNothing().when(categoryService).deleteCategory(anyLong());
        try {
            mockMvc.perform(delete("/shop/categories/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(18)
    @DisplayName(value = "카테고리 전체 조회")
    void getCategories_Success() {
        when(categoryService.getCategories(pageSize, offset)).thenReturn(categoryPage);
        try {
            mockMvc.perform(get("/shop/categories/page")
                    .param("pageSize", "10")
                    .param("offset", "0")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
