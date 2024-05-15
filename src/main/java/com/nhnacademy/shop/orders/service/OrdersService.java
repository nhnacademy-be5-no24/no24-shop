package com.nhnacademy.shop.orders.service;


import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.dto.request.CartPaymentPostRequestDto;
import com.nhnacademy.shop.orders.dto.request.CartPaymentRequestDto;
import com.nhnacademy.shop.orders.dto.request.OrdersCreateRequestResponseDto;
import com.nhnacademy.shop.orders.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 주문(Orders) service
 *
 * @author : 박동희
 * @date : 2024-04-04
 *
 **/
public interface OrdersService {

    /**
     *  (admin) 주문 전체 조회를 위한 method.
     *
     * @return OrdersResponseDto 주문 정보 반환.
     */
    Page<OrdersListForAdminResponseDto> getOrders(Pageable pageable);

    /**
     *  주문 id로 단건 조회를 위한 method.
     *
     * @param orderId 조회할 주문 아이디 입니다.
     * @return OrdersResponseDto 주문 정보가 반환.
     */
    OrdersResponseDto getOrderByOrdersId(String orderId);



    /**
     * 고객 아이디로 주문 조회를 위한 method.
     *
     * @param pageable, customerNo 조회할 customerNo 입니다.
     * @return OrdersResponseDto 주문 정보가 반환됩니다.
     */
    Page<OrdersResponseDto> getOrderByCustomer(Pageable pageable, Long customerNo);

    /**
     * 고객 아이디로 구매 확정된 주문 조회를 위한 method.
     * @param customerNo
     * @return OrderConfirmResponseDto
     */
    OrderConfirmResponseDto getConfirmedOrderByCustomer(Long customerNo);

    /**
     *  주문 등록을 위한 method.
     * @param ordersCreateRequestDto 주문 등록을 위한 정보 입니다.
     * @return OrdersResponseDto 주문 정보가 반환.
     */
    OrdersCreateRequestResponseDto createOrder(OrdersCreateRequestResponseDto ordersCreateRequestDto);


    /**
     * 주문 정보 수정을 위한 method
     *
     * @param orderId 조회할  주문 id.
     * @param orderState 수정할 state 정보
     */
    void modifyOrderState(String orderId, Orders.OrderState orderState);

    /**
     * 장바구니에서 받아서 주문 결제 페이지 정보 반환.
     * @param cartPaymentRequestDto 장바구니 정보.
     * @return CartPaymentResponseDto  포장지, 쿠폰, 책, 유저 정보 반환
     */
    CartPaymentResponseDto getCartPaymentInfo(CartPaymentRequestDto cartPaymentRequestDto);
}
