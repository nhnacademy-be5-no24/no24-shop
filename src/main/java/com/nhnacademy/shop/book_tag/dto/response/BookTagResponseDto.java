package com.nhnacademy.shop.book_tag.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTagResponseDto {
    private Long tagId;
    private String tagName;
}
