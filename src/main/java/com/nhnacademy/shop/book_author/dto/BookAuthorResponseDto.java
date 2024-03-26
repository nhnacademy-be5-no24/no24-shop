package com.nhnacademy.shop.book_author.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookAuthorResponseDto {
    private String bookIsbn;
    private Long authorId;
}
