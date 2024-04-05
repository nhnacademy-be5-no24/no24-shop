package com.nhnacademy.shop.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewContent;
    private Integer reviewScore;
    private String reviewImage;
    private String bookIsbn;
    private Long customerNo;
}
