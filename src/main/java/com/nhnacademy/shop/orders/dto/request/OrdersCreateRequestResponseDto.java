package com.nhnacademy.shop.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.nhnacademy.shop.order_detail.dto.OrderDetailDto;
import com.nhnacademy.shop.orders.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrdersCreateRequestResponseDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String orderId;
    private LocalDateTime orderDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate shipDate;
    private Orders.OrderState orderState;
    private Long totalFee;
    private int deliveryFee;
    private Long paymentId;
    private Long customerNo;
    private String jSessionId;
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String req;
    private Long usedPoint;
    private List<OrderDetailDto> orderDetailDtoList;
}
