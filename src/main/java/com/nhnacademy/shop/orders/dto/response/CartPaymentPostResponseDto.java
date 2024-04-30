package com.nhnacademy.shop.orders.dto.response;


import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.wrap.domain.Wrap;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartPaymentPostResponseDto {
    //고객
    private Long customerNo;
    //주문
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String req;
    //도서 쿠폰 포장지
    private List<BookInfo> bookInfos;
    private Long updateTotalPrice;

    @Getter
    @Builder
    public static class BookInfo{
        private String bookIsbn;
        private String bookTittle;
        private Long updateBookSalePrice;
        private CouponResponseDto couponResponseDto;
        private Wrap wrap;
    }

}
