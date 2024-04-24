package com.nhnacademy.shop.orders.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartPaymentPostRequestDto {
    private Long customerNo;
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String req;

}
