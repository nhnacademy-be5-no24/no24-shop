package com.nhnacademy.shop.orders.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class CartPaymentRequestDto {
    private List<BookInfo> bookInfos;
    private Long customerNo;

    public CartPaymentRequestDto(List<BookInfo> bookInfos, Long customerNo) {
        this.bookInfos = bookInfos;
        this.customerNo = customerNo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    public static class BookInfo{
        private String bookIsbn;
        private Long bookSalePrice;
        private Long quantity;

        public BookInfo(String bookIsbn, Long bookSalePrice, Long quantity) {
            this.bookIsbn = bookIsbn;
            this.bookSalePrice = bookSalePrice;
            this.quantity = quantity;
        }
    }
}
