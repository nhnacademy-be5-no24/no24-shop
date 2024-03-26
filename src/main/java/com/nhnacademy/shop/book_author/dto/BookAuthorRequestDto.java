package com.nhnacademy.shop.book_author.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookAuthorRequestDto {
    private String bookIsbn;
    private Long authorId;
}
