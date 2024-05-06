package com.nhnacademy.shop.order_detail.controller;

import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.order_detail.dto.OrderDetailResponseDtoList;
import com.nhnacademy.shop.order_detail.service.OrderDetailService;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주문 상세 정보를 전달하는 컨트롤러
 *
 * @Author : 박병휘
 * @Date : 2024/05/06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/orders/detail")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDtoList> getOrderDetails(@PathVariable String orderId) {
        OrderDetailResponseDtoList orderDetailResponseDtoList =
                new OrderDetailResponseDtoList(orderDetailService.getOrderDetailsByOrderId(orderId));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderDetailResponseDtoList);
    }
}
