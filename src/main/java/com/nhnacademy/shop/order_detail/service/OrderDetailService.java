package com.nhnacademy.shop.order_detail.service;

import com.nhnacademy.shop.order_detail.dto.OrderDetailDto;
import com.nhnacademy.shop.order_detail.dto.OrderDetailResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orderdetail 반환 서비스
 *
 * @Author : 박병휘
 * @Date : 2024/05/06
 */
@Service
public interface OrderDetailService {
    List<OrderDetailResponseDto> getOrderDetailsByOrderId(String orderId);
}
