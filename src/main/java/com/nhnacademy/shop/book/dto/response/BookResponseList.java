package com.nhnacademy.shop.book.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseList {
    List<BookResponseDto> bookResponseDtoList;
    int maxPage;
}
