package com.nhnacademy.shop.order_detail.service.impl;

import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.order_detail.dto.OrderDetailDto;
import com.nhnacademy.shop.order_detail.dto.OrderDetailResponseDto;
import com.nhnacademy.shop.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.shop.order_detail.service.OrderDetailService;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.exception.NotFoundOrderException;
import com.nhnacademy.shop.orders.repository.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 주문 상세 서비스 구현체
 *
 * @Author : 박병휘
 * @Date : 2024/05/06
 */
@AllArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrdersRepository ordersRepository;

    @Override
    public List<OrderDetailResponseDto> getOrderDetailsByOrderId(String orderId) {
        Optional<Orders> optionalOrder = ordersRepository.findById(orderId);

        if(optionalOrder.isEmpty()) {
            throw new NotFoundOrderException(orderId);
        }

        return orderDetailRepository.findByOrder(optionalOrder.get()).stream()
                .map(orderDetail -> OrderDetailResponseDto.builder()
                        .book(new BookResponseDto(orderDetail.getBook()))
                        .quantity(orderDetail.getAmount())
                        .wraps(orderDetail.getWrapInfos() != null ? orderDetail.getWrapInfos().stream()
                                .map(wrapInfo ->  new OrderDetailResponseDto.WrapDto(
                                        wrapInfo.getWrap().getWrapName(),
                                        wrapInfo.getWrap().getWrapCost(),
                                        wrapInfo.getAmount())
                                )
                                .collect(Collectors.toList()) : List.of())
                        .build()
                ).collect(Collectors.toList());
    }
}
