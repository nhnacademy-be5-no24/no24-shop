package com.nhnacademy.delivery.orders.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrdersModifyResponseDto {
    private String orderId;

    public OrdersModifyResponseDto (String orderId){
        this.orderId = orderId;
    }
}
