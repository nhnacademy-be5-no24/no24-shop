package com.nhnacademy.shop.orders.controller;


import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.dto.request.CartPaymentPostRequestDto;
import com.nhnacademy.shop.orders.dto.request.CartPaymentRequestDto;
import com.nhnacademy.shop.orders.dto.request.OrdersCreateRequestDto;
import com.nhnacademy.shop.orders.dto.response.CartPaymentPostResponseDto;
import com.nhnacademy.shop.orders.dto.response.CartPaymentResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.exception.OrderStatusFailedException;
import com.nhnacademy.shop.orders.exception.SaveOrderFailed;
import com.nhnacademy.shop.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrdersService orderService;


    /**
     * 모든 주문을 반환. (admin)
     * @param pageable 페이징.
     * @return 200 OK, 모든 주문 반환.
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<OrdersListForAdminResponseDto>> getOrders(
            Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrders(pageable));
    }
    /**
     * 고객 번호로 고객의 모든 주문을 반환.
     *
     * @param customerNo 고객 번호.
     * @param pageable 페이징.
     * @return 200, 고객의 모든 주문 반환.
     */
    @GetMapping("/customer/{customerNo}")
    public ResponseEntity<Page<OrdersResponseDto>> getOrdersByCustomer(
            @PathVariable Long customerNo,
            Pageable pageable) {
        Page<OrdersResponseDto> ordersPage = orderService.getOrderByCustomer(pageable, customerNo);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ordersPage);
    }

    /**
     * 주문아이디로 주문 정보를 반환
     *
     * @param orderId 주문 아이디.
     * @return 200 OK, 주문 상세내용을 반환.
     */
    @GetMapping("/orderId/{orderId}")
    public ResponseEntity<OrdersResponseDto> getOrderDetailByOrderId(
            @PathVariable String orderId
    ) {
        OrdersResponseDto responseDto = orderService.getOrderByOrdersId(orderId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    /**
     * 장바구니에서 받아서 주문 결제 페이지 정보 반환.
     * @param cartPaymentRequestDto 장바구니 정보.
     * @return 200 Ok. 포장지, 쿠폰, 책, 유저 정보 반환
     */
    @GetMapping("/cart")
    public ResponseEntity<CartPaymentResponseDto> getCartPaymentInfo(
            @RequestBody CartPaymentRequestDto cartPaymentRequestDto
    ){
        CartPaymentResponseDto responseDto = orderService.getCartPaymentInfo(cartPaymentRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    /**
     *
     * @param ordersCreateRequestDto 주문 등록을 위한 dto.
     * @throws OrderStatusFailedException not found.
     * @throws SaveOrderFailed not found.
     * @return 201 created.
     */
    @PostMapping
    public ResponseEntity<OrdersResponseDto> createOrder(
            @RequestBody OrdersCreateRequestDto ordersCreateRequestDto
            ){
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(orderService.createOrder(ordersCreateRequestDto));
        }catch(OrderStatusFailedException | SaveOrderFailed e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 결제전 페이지에서 쿠폰, 포장지 선택한거 저장하는 method
     * @param cartPaymentPostRequestDto
     * @return
     */
    @PostMapping("/cart")
    public ResponseEntity<CartPaymentPostResponseDto> createOrderCart(
            @RequestBody CartPaymentPostRequestDto cartPaymentPostRequestDto
    ){

        CartPaymentResponseDto dto = orderService.createCartPamentInfo(cartPaymentPostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);

        //쿠폰, 포장 애들 선택하고  (포장지 수량 down) 총가격줘야하는듯..?
        //transaction 생각해야할듯하오.
    }


    /**
     * 주문 상태 바꾸는 method.
     *
     * @param orderId 주문 아이디
     * @param orderState 주문상태
     *
     * @return 201, created 반환
     */
    @PutMapping("/{orderId}/state")
    public ResponseEntity<Void> modifyOrderState(
            @PathVariable String orderId,
            @RequestParam Orders.OrderState orderState
            ){

        orderService.modifyOrderState(orderId, orderState);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
