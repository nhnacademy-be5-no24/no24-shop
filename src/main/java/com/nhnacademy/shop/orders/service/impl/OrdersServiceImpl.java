package com.nhnacademy.delivery.orders.service.impl;


import com.nhnacademy.delivery.orders.domain.Orders;
import com.nhnacademy.delivery.orders.dto.request.OrdersCreateRequestDto;
import com.nhnacademy.delivery.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.delivery.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.delivery.orders.exception.NotFoundOrderException;
import com.nhnacademy.delivery.orders.exception.OrderStatusFailedException;
import com.nhnacademy.delivery.orders.exception.SaveOrderFailed;
import com.nhnacademy.delivery.orders.repository.OrdersRepository;
import com.nhnacademy.delivery.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 주문 서비스의 구현체입니다.
 *
 * @author : 박동희
 * @date : 2024-04-05
 **/
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;


    // 주문리스트 전체 가져오기(admin)
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersListForAdminResponseDto> getOrders(Pageable pageable) {
       return ordersRepository.getOrderList(pageable);
    }

    // 주문아이디로 상품리스트 가져오기
    @Override
    @Transactional(readOnly = true)
    public OrdersResponseDto getOrderByOrdersId(String orderId) {
        Optional<OrdersResponseDto> optionalOrders = ordersRepository.getOrderByOrderId(orderId);
        if(optionalOrders.isEmpty()){
            throw new NotFoundOrderException(orderId);
        }
        return optionalOrders.get();
    }

    // 고객번호로 상품리스트 들고오기
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersResponseDto> getOrderByCustomer(
            Pageable pageable, Long customerNo) {
        return ordersRepository.getOrderListByCustomer(pageable, customerNo);
    }


    // 결제 완료되면 주문 저장하기
    @Override
    @Transactional
    public OrdersResponseDto createOrder(OrdersCreateRequestDto ordersCreateRequestDto) {
        Orders.OrderState orderState = ordersCreateRequestDto.getOrderState();
        if (orderState != Orders.OrderState.COMPLETE_PAYMENT) {
            throw new OrderStatusFailedException("Invalid order state: " + orderState);
        }
        Orders orders = Orders.builder()
                .orderDate(LocalDate.now())
                .orderState(Orders.OrderState.valueOf("WAITING"))
                .deliveryFee(ordersCreateRequestDto.getDeliveryFee())
                .payment(ordersCreateRequestDto.getPayment())
                .customer(ordersCreateRequestDto.getCustomer())
                .receiverName(ordersCreateRequestDto.getReceiverName())
                .receiverPhoneNumber(ordersCreateRequestDto.getReceiverPhoneNumber())
                .zipcode(ordersCreateRequestDto.getZipcode())
                .address(ordersCreateRequestDto.getAddress())
                .addressDetail(ordersCreateRequestDto.getAddressDetail())
                .req(ordersCreateRequestDto.getReq())
                .orderDetails(ordersCreateRequestDto.getOrderDetailList())
                .build();

        Orders createdOrders = ordersRepository.save(orders);
        Optional<OrdersResponseDto> ordersResponseDto = ordersRepository.getOrderByOrderId(createdOrders.getOrderId());

        if(ordersResponseDto.isPresent()){
            return ordersResponseDto.get();
        }else{
            throw new SaveOrderFailed(ordersResponseDto.get().getOrderId());
        }
    }
    // 주문 상태 변경
    @Override
    public void modifyOrderState(String orderId, Orders.OrderState orderstate) {

        Optional<Orders> optionalOrders = ordersRepository.findById(orderId);
        if (optionalOrders.isEmpty()) {
            throw new NotFoundOrderException(orderId);
        }

        Orders orders = optionalOrders.get();

        orders.modifyState(orderstate);
        ordersRepository.save(orders);


    }
//    //주문결제페이지 정보 만들기
//    @Override
//    public CartPaymentResponseDto getCartPaymentInfo(CartPaymentRequestDto cartPaymentRequestDto) {
//        // todo : customer req(customerNo), response(customerNo, name, phoneNumber) 이어주기
//        // todo 2 : 받는 사람 정보 가져오기 ... 이건 default로 주소지에서 가져와야할 것 같은데 -> 병주한테 물어봐야함 주소-회원 연결했는지.
//
//    }


}
