package com.nhnacademy.shop.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 저자 등록을 위한 request dto 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequestDto {
    private String authorName;
}
