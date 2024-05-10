package com.nhnacademy.shop.orders.dto.response;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.wrap.domain.Wrap;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CartPaymentResponseDto {
    //포장, 쿠폰, 북
    private List<BookInfo> bookInfos;
    private Long totalPrice;
    //고객
    private Long customerNo;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    //주문
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String req;

    @Getter
    @Builder
    public static class BookInfo{
        private String bookIsbn;
        private String bookTitle;
        private Long bookSalePrice;
        private Long quantity;
        private List<CouponMemberResponseDto> coupons;
        private List<Wrap> wraps;

    }
}
