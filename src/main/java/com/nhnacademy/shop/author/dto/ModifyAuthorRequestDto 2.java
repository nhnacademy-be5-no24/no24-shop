package com.nhnacademy.shop.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 저자 수정을 위한 request dto 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyAuthorRequestDto {
    private Long authorId;
    private String authorName;
}