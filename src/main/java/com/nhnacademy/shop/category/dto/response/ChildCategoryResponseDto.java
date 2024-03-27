package com.nhnacademy.shop.category.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChildCategoryResponseDto {
    private Long categoryId;
    private String categoryName;

    public ChildCategoryResponseDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
