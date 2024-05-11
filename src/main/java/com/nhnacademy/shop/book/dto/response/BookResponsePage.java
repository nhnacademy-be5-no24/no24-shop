package com.nhnacademy.shop.book.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * BookList 객체를 받기 위한 dto
 *
 * @Author : 박병휘
 * @Date : 2024/04/24
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponsePage {
    private List<BookResponseDto> content;

    public void setContent(List<BookResponseDto> content) {
        this.content = content;
    }
}
