package com.nhnacademy.delivery.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.delivery.customer.domain.Customer;

import com.nhnacademy.delivery.order_detail.domain.OrderDetail;
import com.nhnacademy.delivery.orders.domain.Orders;
import com.nhnacademy.delivery.payment.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrdersCreateRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate orderDate;
    private Orders.OrderState orderState;
    private Long deliveryFee;
    private Payment payment;
    private Customer customer;
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String req;
    private List<OrderDetail> orderDetailList;
}
