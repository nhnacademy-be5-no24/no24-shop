package com.nhnacademy.shop.orders.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.shop.orders.domain.Orders;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrdersResponseDto {
    private String orderId;
    private String bookTitle;
    private Long bookSalePrice;
    private String wrapName;
    private Long wrapCost;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime orderDate;
    private String receiverName;
    private String receiverPhoneNumber;
    private String address;
    private String addressDetail;
    private Orders.OrderState orderState;

    public OrdersResponseDto(String orderId,
                             String bookTitle,
                             Long bookSalePrice,
                             String wrapName,
                             Long wrapCost,
                             LocalDateTime orderDate,
                             String receiverName,
                             String receiverPhoneNumber,
                             String address,
                             String addressDetail,
                             Orders.OrderState orderState) {
        this.orderId = orderId;
        this.bookTitle = bookTitle;
        this.bookSalePrice = bookSalePrice;
        this.wrapName = wrapName;
        this.wrapCost = wrapCost;
        this.orderDate = orderDate;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.address = address;
        this.addressDetail = addressDetail;
        this.orderState = orderState;
    }
}
