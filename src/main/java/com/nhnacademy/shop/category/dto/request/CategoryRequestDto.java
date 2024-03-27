package com.nhnacademy.shop.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    @NotBlank(message = "카테고리 이름이 비어있습니다. 이름을 입력해주세요.")
    @NotNull
    @Length(min = 1, max = 20, message = "카테고리 이름은 20자까지 입력할 수 있습니다.")
    private String categoryName;
    private Long parentCategoryId;
}
