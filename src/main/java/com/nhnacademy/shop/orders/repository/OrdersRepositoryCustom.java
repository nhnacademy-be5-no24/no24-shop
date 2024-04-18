package com.nhnacademy.delivery.orders.repository;

import com.nhnacademy.delivery.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.delivery.orders.dto.response.OrdersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;


@NoRepositoryBean
public interface OrdersRepositoryCustom {
    /**
     * 모든 주문을 반환합니다.
     *
     * @param pageable 페이징.
     * @return OrderListForAdminResponseDto 모든 주문을 반환.
     */
    Page<OrdersListForAdminResponseDto> getOrderList(Pageable pageable);

    /**
     * 멤버의 모든 주문을 반환합니다.
     *
     * @param pageable 페이징.
     * @param customerNo 고객 번호.
     * @return OrderResponseDto 멤버의 모든 주문 반환.
     */
    Page<OrdersResponseDto> getOrderListByCustomer(Pageable pageable, Long customerNo);

    /**
     * 주문 id로 주문을 반환합니다.
     *
     * @param orderId 주문 아이디.
     * @return OrderResponseDto 주문 정보를 반환.
     */
    Optional<OrdersResponseDto> getOrderByOrderId(String orderId);





}
