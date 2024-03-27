package com.nhnacademy.shop.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class ParentCategoryResponseDto {
    private Long categoryId;
    private String categoryName;

    // TODO #3 자식 카테고리 출력 필요하면 수정해야함
    @Setter
    private List<ChildCategoryResponseDto> childCategories;

    public ParentCategoryResponseDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
