package com.nhnacademy.shop.category.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 하위 카테고리 기본정보 반환을 위한 dto 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
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
