package com.nhnacademy.shop.category.service.impl;

import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.request.CategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.category.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryResponseDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), null);
    }

    @Override
    public CategoryResponseDto getCategoryByCategoryName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        if(Objects.isNull(category)) {
            throw new CategoryNotFoundException();
        }
        if(Objects.isNull(category.getParentCategory())) {
            return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), null);
        }
        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), category.getParentCategory().getCategoryId());
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category parentCategory = getParentCategory(categoryRequestDto.getParentCategoryId());
        if (Objects.nonNull(categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName()))) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = new Category();
        category.setCategoryName(categoryRequestDto.getCategoryName());
        category.setParentCategory(parentCategory);
        Category savedCategory = categoryRepository.save(category);

        if (Objects.nonNull(savedCategory.getParentCategory())) {
            return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), savedCategory.getParentCategory().getCategoryId());
        }
        return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), null);
    }

    @Override
    public CategoryResponseDto modifyCategory(ModifyCategoryRequestDto modifyCategoryRequestDto) {
        Category parentCategory = getParentCategory(modifyCategoryRequestDto.getParentCategoryId());
        if (Objects.nonNull(categoryRepository.findByCategoryName(modifyCategoryRequestDto.getCategoryName()))) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = new Category();
        category.setCategoryId(modifyCategoryRequestDto.getCategoryId());
        category.setCategoryName(modifyCategoryRequestDto.getCategoryName());
        category.setParentCategory(parentCategory);
        Category savedCategory = categoryRepository.save(category);

        if (Objects.nonNull(savedCategory.getParentCategory())) {
            return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), savedCategory.getParentCategory().getCategoryId());
        }
        return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), null);
    }

    @Override
    public ParentCategoryResponseDto modifyParentCategory(ModifyCategoryRequestDto modifyCategoryRequestDto) {
        if (Objects.nonNull(categoryRepository.findByCategoryName(modifyCategoryRequestDto.getCategoryName()))) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = new Category();
        category.setCategoryId(modifyCategoryRequestDto.getCategoryId());
        category.setCategoryName(modifyCategoryRequestDto.getCategoryName());
        Category savedCategory = categoryRepository.save(category);

        return new ParentCategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<ParentCategoryResponseDto> getParentWithChildCategories() {
        List<ParentCategoryResponseDto> parentCategoryResponseDtoList = categoryRepository.getParentCategoriesWithChildCategories();
        if(Objects.isNull(parentCategoryResponseDtoList)) {
            throw new CategoryNotFoundException();
        }
        return parentCategoryResponseDtoList;
    }

    @Override
    public List<ParentCategoryInfoResponseDto> getParentCategories() {
        List<ParentCategoryInfoResponseDto> parentCategoryInfoResponseDtoList = categoryRepository.getParentCategories();
        if(Objects.isNull(parentCategoryInfoResponseDtoList)) {
            throw new CategoryNotFoundException();
        }
        return parentCategoryInfoResponseDtoList;
    }

    @Override
    public ParentCategoryResponseDto getParentWithChildCategoryByParentCategoryId(Long categoryId) {
        ParentCategoryResponseDto parentCategoryResponseDto = categoryRepository.getParentCategory(categoryId);
        if (Objects.isNull(parentCategoryResponseDto)) {
            throw new CategoryNotFoundException();
        }
        return parentCategoryResponseDto;
    }

    private Category getParentCategory(Long categoryId) {
        if(Objects.isNull(categoryId)) {
            return null;
        }
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

}
