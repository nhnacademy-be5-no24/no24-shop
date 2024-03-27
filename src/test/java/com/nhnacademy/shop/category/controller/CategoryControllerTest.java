package com.nhnacademy.shop.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.category.dto.request.CategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    private ObjectMapper objectMapper = new ObjectMapper();

    CategoryRequestDto categoryRequestDto;

    ModifyCategoryRequestDto modifyCategoryRequestDto;

    @BeforeEach
    void setUp() {
        categoryRequestDto = new CategoryRequestDto("로맨스", null);
        modifyCategoryRequestDto = new ModifyCategoryRequestDto(1L, "판타지", null);
    }

    // TODO dto test 필요
    @Test
    void createCategoryTest_Success() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);
        doNothing().when(categoryService).createCategory(categoryRequestDto);
        try {
            mockMvc.perform(post("/shop/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoryRequestDto)))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createCategoryTest_Conflict() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);
        doThrow(CategoryAlreadyExistsException.class).when(categoryService).createCategory(categoryRequestDto);
        try {
            mockMvc.perform(post("/shop/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(categoryRequestDto)))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCategoryTest_Success() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);
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
    void getCategoryTest_NotFound() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);
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
    void getCategoryByNameTest_Success() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);
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
    void getCategoryByNameTest_NotFound() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);
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
    void getCategoriesTest_Success() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);

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
    void getCategoriesTest_NotFound() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);

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
    void getParentCategoriesTest_Success() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);

        List<ParentCategoryInfoResponseDto> dtoList = new ArrayList<>();
        when(categoryService.getParentCategories()).thenReturn(dtoList);
        try {
            mockMvc.perform(get("/shop/categories/parents")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getParentCategoriesTest_NotFound() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);

        when(categoryService.getParentCategories()).thenReturn(null);
        try {
            mockMvc.perform(get("/shop/categories/parents")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getParentWithChildCategoryTest_Success() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);

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
    void getParentWithChildCategoryTest_NotFound() {
        ReflectionTestUtils.setField(categoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(categoryRequestDto, "parentCategoryId", null);

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
    void modifyParentCategoryTest_Success() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        doNothing().when(categoryService).modifyParentCategory(modifyCategoryRequestDto);
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
    void modifyCategoryTest_Success() {
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryId", 1L);
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "categoryName", "hello");
        ReflectionTestUtils.setField(modifyCategoryRequestDto, "parentCategoryId", null);
        doNothing().when(categoryService).modifyCategory(modifyCategoryRequestDto);
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

}
