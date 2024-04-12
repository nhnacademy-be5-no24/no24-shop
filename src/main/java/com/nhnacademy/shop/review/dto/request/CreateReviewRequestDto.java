package com.nhnacademy.shop.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {
    @NotNull
    @NotBlank(message = "리뷰 내용을 작성해주세요.")
    private String reviewContent;
    @NotNull
    @Min(value = 1, message = "평점은 1점 ~ 5점까지 입력 가능합니다.")
    @Max(value = 5, message = "평점은 1점 ~ 5점까지 입력 가능합니다.")
    private Integer reviewScore;
    private String reviewImage;
    @NotNull
    @NotBlank(message = "도서 고유 번호를 입력해주세요.")
    private String bookIsbn;
    @NotNull(message = "회원 번호를 입력해주세요.")
    private Long customerNo;
}
