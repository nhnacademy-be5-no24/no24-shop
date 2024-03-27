package com.nhnacademy.shop.category.controller;

import com.nhnacademy.shop.category.dto.request.CategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.service.CategoryService;
import com.nhnacademy.shop.member.exception.MemberForbiddenException;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/shop")
public class CategoryController {
    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long categoryId) {
        CategoryResponseDto categoryResponseDto = categoryService.getCategory(categoryId);
        if (Objects.isNull(categoryResponseDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found : " + categoryId);
        }
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping("/categories/name/{categoryName}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String categoryName) {
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryByCategoryName(categoryName);
        if (Objects.isNull(categoryResponseDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found : " + categoryName);
        }
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ParentCategoryResponseDto>> getParentWithChildCategories() {
        List<ParentCategoryResponseDto> categoryResponseDtoList = categoryService.getParentWithChildCategories();
        if (Objects.isNull(categoryResponseDtoList)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categories Not Found");
        }
        return ResponseEntity.ok(categoryResponseDtoList);
    }

    @GetMapping("/categories/parents")
    public ResponseEntity<List<ParentCategoryInfoResponseDto>> getParentCategories() {
        List<ParentCategoryInfoResponseDto> categoryResponseDtoList = categoryService.getParentCategories();
        if (Objects.isNull(categoryResponseDtoList)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categories Not Found");
        }
        return ResponseEntity.ok(categoryResponseDtoList);
    }

    @GetMapping("/categories/parents/{categoryId}")
    public ResponseEntity<ParentCategoryResponseDto> getParentWithChildCategory(@PathVariable Long categoryId) {
        ParentCategoryResponseDto parentCategory = categoryService.getParentWithChildCategoryByParentCategoryId(categoryId);
        if (Objects.isNull(parentCategory)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found : " + categoryId);
        }
        return ResponseEntity.ok(parentCategory);
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        try {
            categoryService.createCategory(categoryRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (CategoryAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/categories")
    public ResponseEntity<CategoryResponseDto> modifyCategory(@RequestBody @Valid ModifyCategoryRequestDto modifyCategoryRequestDto) {
        try {
            categoryService.modifyCategory(modifyCategoryRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (CategoryAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/categories/parents")
    public ResponseEntity<ParentCategoryResponseDto> modifyParentCategory(@RequestBody @Valid ModifyCategoryRequestDto modifyCategoryRequestDto) {
        try {
            categoryService.modifyParentCategory(modifyCategoryRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (CategoryAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
