package com.nhnacademy.shop.like.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요 관리 DTO
 *
 * @author : 이재원
 * @date : 2024-04-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LikeMemberBookReqeustDto {

    @JsonProperty("customer_no")
    private Long customerNo;

    @JsonProperty("book_isbn")
    private String bookIsbn;
    
}
