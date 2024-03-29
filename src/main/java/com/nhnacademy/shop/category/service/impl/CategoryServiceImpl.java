package com.nhnacademy.shop.category.service.impl;

import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.dto.request.CreateCategoryRequestDto;
import com.nhnacademy.shop.category.dto.request.ModifyCategoryRequestDto;
import com.nhnacademy.shop.category.dto.response.CategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.CategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.category.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 카테고리 서비스 구현체 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    /**
     * {@inheritDoc}
     * @throws CategoryNotFoundException 카테고리가 존재 하지 않을 때 발생하는 Exception 입니다.
     */
    @Override
    public CategoryResponseDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), null);
    }

    /**
     * {@inheritDoc}
     * @throws CategoryNotFoundException 카테고리가 존재 하지 않을 때 발생하는 Exception 입니다.
     */
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

    /**
     * {@inheritDoc}
     * @throws CategoryAlreadyExistsException 이미 존재하는 카테고리 생성 혹은 수정 요청 시 발생하는 Exception 입니다.
     */
    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto createCategoryRequestDto) {
        Category parentCategory = getParentCategory(createCategoryRequestDto.getParentCategoryId());
        if (Objects.nonNull(categoryRepository.findByCategoryName(createCategoryRequestDto.getCategoryName()))) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = Category.builder()
                .categoryName(createCategoryRequestDto.getCategoryName())
                .parentCategory(parentCategory)
                .build();
        Category savedCategory = categoryRepository.save(category);

        if (Objects.nonNull(savedCategory.getParentCategory())) {
            return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), savedCategory.getParentCategory().getCategoryId());
        }
        return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), null);
    }

    /**
     * {@inheritDoc}
     * @throws CategoryAlreadyExistsException 이미 존재하는 카테고리 생성 혹은 수정 요청 시 발생하는 Exception 입니다.
     */
    @Override
    public CategoryResponseDto modifyCategory(ModifyCategoryRequestDto modifyCategoryRequestDto) {
        Category parentCategory = getParentCategory(modifyCategoryRequestDto.getParentCategoryId());
        if (Objects.nonNull(categoryRepository.findByCategoryName(modifyCategoryRequestDto.getCategoryName()))) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = Category.builder()
                .categoryId(modifyCategoryRequestDto.getCategoryId())
                .categoryName(modifyCategoryRequestDto.getCategoryName())
                .parentCategory(parentCategory)
                .build();
        Category savedCategory = categoryRepository.save(category);

        if (Objects.nonNull(savedCategory.getParentCategory())) {
            return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), savedCategory.getParentCategory().getCategoryId());
        }
        return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName(), null);
    }

    /**
     * {@inheritDoc}
     * @throws CategoryAlreadyExistsException 이미 존재하는 카테고리 생성 혹은 수정 요청 시 발생하는 Exception 입니다.
     */
    @Override
    public ParentCategoryResponseDto modifyParentCategory(ModifyCategoryRequestDto modifyCategoryRequestDto) {
        if (Objects.nonNull(categoryRepository.findByCategoryName(modifyCategoryRequestDto.getCategoryName()))) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = Category.builder()
                .categoryId(modifyCategoryRequestDto.getCategoryId())
                .categoryName(modifyCategoryRequestDto.getCategoryName())
                .build();
        Category savedCategory = categoryRepository.save(category);

        return new ParentCategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getCategoryName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    /**
     * {@inheritDoc}
     * @throws CategoryNotFoundException 카테고리가 존재 하지 않을 때 발생하는 Exception 입니다.
     */
    @Override
    public List<ParentCategoryResponseDto> getParentWithChildCategories() {
        List<ParentCategoryResponseDto> parentCategoryResponseDtoList = categoryRepository.getParentCategoriesWithChildCategories();
        if(Objects.isNull(parentCategoryResponseDtoList)) {
            throw new CategoryNotFoundException();
        }
        return parentCategoryResponseDtoList;
    }

    /**
     * {@inheritDoc}
     * @throws CategoryNotFoundException 카테고리가 존재 하지 않을 때 발생하는 Exception 입니다.
     */
    @Override
    public List<CategoryInfoResponseDto> getCategoriesInfo() {
        List<CategoryInfoResponseDto> categoryInfoResponseDtoList = categoryRepository.getCategoriesInfo();
        if(Objects.isNull(categoryInfoResponseDtoList)) {
            throw new CategoryNotFoundException();
        }
        return categoryInfoResponseDtoList;
    }

    /**
     * {@inheritDoc}
     * @throws CategoryNotFoundException 카테고리가 존재 하지 않을 때 발생하는 Exception 입니다.
     */
    @Override
    public ParentCategoryResponseDto getParentWithChildCategoryByParentCategoryId(Long categoryId) {
        ParentCategoryResponseDto parentCategoryResponseDto = categoryRepository.getParentCategory(categoryId);
        if (Objects.isNull(parentCategoryResponseDto)) {
            throw new CategoryNotFoundException();
        }
        return parentCategoryResponseDto;
    }

    /**
     * {@inheritDoc}
     * @throws CategoryNotFoundException 카테고리가 존재 하지 않을 때 발생하는 Exception 입니다.
     */
    private Category getParentCategory(Long categoryId) {
        if(Objects.isNull(categoryId)) {
            return null;
        }
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

}
