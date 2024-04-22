package com.nhnacademy.shop.review.exception;

/**
 * 리뷰을 찾지 못 했을 때 발생하는 Exception 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-01
 */
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException() {
        super("해당 리뷰를 찾을 수 없습니다.");
    }
}
