package com.nhnacademy.shop.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyReviewRequestDto {
    @NotNull
    @NotBlank(message = "수정할 리뷰 아이디를 입력해주세요.")
    private Long reviewId;
    @NotNull
    @NotBlank(message = "리뷰 내용을 작성해주세요.")
    private String reviewContent;
    @NotNull
    @Size(min = 1, max = 5, message = "평점은 1점 ~ 5점까지 입력 가능합니다.")
    private Integer reviewScore;
    private String reviewImage;
    @NotNull
    @NotBlank(message = "도서 고유 번호를 입력해주세요.")
    private String bookIsbn;
    @NotNull
    @NotBlank(message = "회원 번호를 입력해주세요.")
    private Long customerNo;
}
