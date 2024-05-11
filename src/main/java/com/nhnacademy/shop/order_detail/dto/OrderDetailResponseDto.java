package com.nhnacademy.shop.order_detail.dto;

import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import lombok.*;

import java.util.List;

/**
 * ItemDto (temp)
 * todo: Book으로 수정이 필요함
 *
 * @Author : 박병휘
 * @Date : 2024/04/10
 */
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderDetailResponseDto {
    BookResponseDto book;
    Long quantity;
    List<WrapDto> wraps;

    @Getter
    @NoArgsConstructor
    @Builder
    public static class WrapDto {
        String wrapName;
        Long wrapCost;
        Long quantity;

        public WrapDto(String wrapName, Long wrapCost, Long quantity) {
            this.wrapName = wrapName;
            this.wrapCost = wrapCost;
            this.quantity = quantity;
        }
    }
}