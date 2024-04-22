package com.nhnacademy.shop.orders.dto.response;


import com.nhnacademy.shop.orders.domain.Orders;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@Builder
public class  OrdersListForAdminResponseDto {
    private String orderId;
    private String customerName;
    private LocalDate orderDate;
    private Orders.OrderState orderState;
    private String address;
    private String addressDetail;
    private String wrapName;
    private Long wrapCost;
    private String bookTitle;
    private Long bookSalePrice;

    public OrdersListForAdminResponseDto(String orderId,
                                         String customerName,
                                         LocalDate orderDate,
                                         Orders.OrderState orderState,
                                         String address,
                                         String addressDetail,
                                         String wrapName,
                                         Long wrapCost,
                                         String bookTitle,
                                         Long bookSalePrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.orderState = orderState;
        this.address = address;
        this.addressDetail = addressDetail;
        this.wrapName = wrapName;
        this.wrapCost = wrapCost;
        this.bookTitle = bookTitle;
        this.bookSalePrice = bookSalePrice;
    }
}