package com.nhnacademy.shop.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 저자 기본정보를 반환하기 위한 response dto 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorResponseDto {
    private Long authorId;
    private String authorName;
}
