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
public class ModifyCategoryRequestDto {
    @NotNull(message = "수정할 카테고리 번호를 입력해주세요.")
    private Long categoryId;
    @NotNull
    @NotBlank(message = "카테고리 이름이 비어있습니다. 수정할 이름을 입력해주세요.")
    @Length(min = 1, max = 20, message = "카테고리 이름은 20자까지 입력할 수 있습니다.")
    private String categoryName;
    private Long parentCategoryId;
}
