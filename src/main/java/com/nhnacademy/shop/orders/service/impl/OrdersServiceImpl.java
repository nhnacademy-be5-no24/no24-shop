package com.nhnacademy.shop.orders.service.impl;


import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.dto.request.CartPaymentPostRequestDto;
import com.nhnacademy.shop.orders.dto.request.CartPaymentRequestDto;
import com.nhnacademy.shop.orders.dto.request.OrdersCreateRequestDto;
import com.nhnacademy.shop.orders.dto.response.CartPaymentPostResponseDto;
import com.nhnacademy.shop.orders.dto.response.CartPaymentResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.exception.NotFoundOrderException;
import com.nhnacademy.shop.orders.exception.OrderStatusFailedException;
import com.nhnacademy.shop.orders.exception.SaveOrderFailed;
import com.nhnacademy.shop.orders.repository.OrdersRepository;
import com.nhnacademy.shop.orders.service.OrdersService;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CouponMemberRepository couponMemberRepository;
    private final WrapRepository wrapRepository;
    private final BookRepository bookRepository;


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
    //주문결제페이지 정보 만들기
    @Override
    public CartPaymentResponseDto getCartPaymentInfo(CartPaymentRequestDto cartPaymentRequestDto) {

        Optional<Customer> optionalCustomer = customerRepository.findById(cartPaymentRequestDto.getCustomerNo());
        if (optionalCustomer.isEmpty()) {
            throw new MemberNotFoundException();
        }

        List<Address> addressList = addressRepository.findByMemberCustomerNo(cartPaymentRequestDto.getCustomerNo());
        Optional<Address> defaultAddressOptional = addressList.stream()
                .filter(Address::getIsDefault)
                .findFirst();
        Address defaultAddress = defaultAddressOptional.orElse(new Address());

        List<CartPaymentResponseDto.BookInfo> list = new ArrayList<>();
        Long totalPrice = 0L;

        for (CartPaymentRequestDto.BookInfo requestBookInfo : cartPaymentRequestDto.getBookInfos()){
            String bookIsbn = requestBookInfo.getBookIsbn();
            Optional<Book> optionalBook = bookRepository.findByBookIsbn(bookIsbn);

            String bookTitle;
            if (optionalBook.isPresent()) {
                bookTitle = optionalBook.get().getBookTitle();
            } else {
                throw new BookNotFoundException();
            }

            Page<CouponResponseDto> couponResponseDtos = couponMemberRepository.findByMemberCustomerNo(cartPaymentRequestDto.getCustomerNo(), PageRequest.of(0,10));
            List<Wrap> wraps = wrapRepository.findAll();
            totalPrice += requestBookInfo.getBookSalePrice();
            CartPaymentResponseDto.BookInfo responseBookInfo = CartPaymentResponseDto.BookInfo.builder()
                    .bookIsbn(bookIsbn)
                    .bookTitle(bookTitle)
                    .bookSalePrice(requestBookInfo.getBookSalePrice())
                    .quantity(requestBookInfo.getQuantity())
                    .coupons(couponResponseDtos.getContent())
                    .wraps(wraps)
                    .build();

            list.add(responseBookInfo);
        }

        return CartPaymentResponseDto.builder()
                .customerNo(optionalCustomer.get().getCustomerNo())
                .customerName(optionalCustomer.get().getCustomerName())
                .customerPhoneNumber(optionalCustomer.get().getCustomerPhoneNumber())
                .receiverName(defaultAddress.getReceiverName())
                .receiverPhoneNumber(defaultAddress.getReceiverPhoneNumber())
                .zipcode(defaultAddress.getZipcode())
                .address(defaultAddress.getAddress())
                .addressDetail(defaultAddress.getAddressDetail())
                .req("요청사항 입력")
                .bookInfos(list)
                .totalPrice(totalPrice < 30000 ? totalPrice + 3000 : totalPrice)
                .build();
    }

    @Override
    public CartPaymentPostResponseDto createCartPaymentInfo(CartPaymentPostRequestDto cartPaymentPostRequestDto) {
        return null;
    }


}
