package com.nhnacademy.shop.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentCategoryInfoResponseDto {
    private Long categoryId;
    private String categoryName;
}
