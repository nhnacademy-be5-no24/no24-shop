package com.nhnacademy.delivery.orders.dto.request;

import com.nhnacademy.delivery.orders.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문을 위한 request dto 입니다.
 *
 * @author : 박동희
 * @date : 2024-04-05
 **/
@Getter
@AllArgsConstructor
@Builder
public class OrdersModifyOrderStateRequestDto {
    private Orders.OrderState orderState;
}
