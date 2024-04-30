package com.nhnacademy.shop.orders.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class OrdersListForAdminResponseDtoList {
    List<OrdersListForAdminResponseDto> ordersListForAdminResponseDtos;

    public OrdersListForAdminResponseDtoList(List<OrdersListForAdminResponseDto> ordersListForAdminResponseDtos) {
        this.ordersListForAdminResponseDtos = ordersListForAdminResponseDtos;
    }
}
