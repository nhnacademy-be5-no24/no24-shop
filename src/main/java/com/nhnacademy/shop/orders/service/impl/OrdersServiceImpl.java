package com.nhnacademy.shop.orders.service.impl;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.category.service.CategoryService;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.repository.CategoryCouponRepository;
import com.nhnacademy.shop.coupon.repository.CouponRepository;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.exception.CustomerNotFoundException;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.order_detail.dto.OrderDetailDto;
import com.nhnacademy.shop.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.dto.request.CartPaymentRequestDto;
import com.nhnacademy.shop.orders.dto.request.OrdersCreateRequestResponseDto;
import com.nhnacademy.shop.orders.dto.response.CartPaymentResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.exception.NotFoundOrderException;
import com.nhnacademy.shop.orders.exception.OrderStatusFailedException;
import com.nhnacademy.shop.orders.exception.TooManyOrderException;
import com.nhnacademy.shop.orders.repository.OrdersRepository;
import com.nhnacademy.shop.orders.service.OrdersService;
import com.nhnacademy.shop.payment.domain.Payment;
import com.nhnacademy.shop.payment.exception.PaymentNotFoundException;
import com.nhnacademy.shop.payment.repository.PaymentRepository;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.repository.PointLogRepository;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapException;
import com.nhnacademy.shop.wrap.exception.TooManyWrapForAmountException;
import com.nhnacademy.shop.wrap.repository.WrapInfoRepository;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WrapInfoRepository wrapInfoRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;
    private final GradeRepository gradeRespository;
    private final CouponRepository couponRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final BookCategoryRepository bookCategoryRepository;
    private final CategoryCouponRepository categoryCouponRepository;
    private final GradeRepository gradeRepository;


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
        Customer customer = customerRepository.findById(customerNo).get();
        List<OrdersResponseDto> orders = ordersRepository.findByCustomer(customer).stream()
                .map(order -> {
                            int amount = order.getOrderDetails().stream()
                                    .mapToInt(o -> Math.toIntExact(o.getAmount()))
                                    .sum();
                            String title = amount != 1 ? order.getOrderDetails().get(0).getBook().getBookTitle() + " 외 " + amount + "권"
                                    : order.getOrderDetails().get(0).getBook().getBookTitle();

                            OrdersResponseDto ordersReponseDto = OrdersResponseDto.builder()
                                    .orderId(order.getOrderId())
                                    .bookTitle(title)
                                    .orderDate(order.getOrderDate())
                                    .receiverName(order.getReceiverName())
                                    .receiverPhoneNumber(order.getReceiverPhoneNumber())
                                    .address(order.getAddress())
                                    .addressDetail(order.getAddressDetail())
                                    .orderState(order.getOrderState())
                                    .totalPrice(order.getTotalFee())
                                    .build();
                            return ordersReponseDto;
                        }
                ).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = (int) Math.min(start + pageable.getPageSize(), orders.size());

        return new PageImpl<>(orders.subList(start, end), pageable, orders.size());
    }


    // 결제 완료되면 주문 저장하기
    @Override
    @Transactional
    public OrdersCreateRequestResponseDto createOrder(OrdersCreateRequestResponseDto ordersCreateRequestResponseDto) {
        Orders.OrderState orderState = ordersCreateRequestResponseDto.getOrderState();
        if (orderState != Orders.OrderState.COMPLETE_PAYMENT) {
            throw new OrderStatusFailedException("Invalid order state: " + orderState);
        }

        // Payment get
        Optional<Payment> optionalPayment = paymentRepository.findById(ordersCreateRequestResponseDto.getPaymentId());

        if(optionalPayment.isEmpty()) {
            throw new PaymentNotFoundException();
        }

        // Customer get
        Optional<Customer> optionalCustomer = customerRepository.findById(ordersCreateRequestResponseDto.getCustomerNo());

        if(optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Orders orders = Orders.builder()
                .orderId(ordersCreateRequestResponseDto.getOrderId())
                .orderDate(LocalDateTime.now())
                .orderState(Orders.OrderState.valueOf("WAITING"))
                .shipDate(ordersCreateRequestResponseDto.getShipDate())
                .totalFee(ordersCreateRequestResponseDto.getTotalFee())
                .deliveryFee(ordersCreateRequestResponseDto.getDeliveryFee())
                .payment(optionalPayment.get())
                .customer(optionalCustomer.get())
                .receiverName(ordersCreateRequestResponseDto.getReceiverName())
                .receiverPhoneNumber(ordersCreateRequestResponseDto.getReceiverPhoneNumber())
                .zipcode(ordersCreateRequestResponseDto.getZipcode())
                .address(ordersCreateRequestResponseDto.getAddress())
                .addressDetail(ordersCreateRequestResponseDto.getAddressDetail())
                .req(ordersCreateRequestResponseDto.getReq())
                .orderDetails(null)
                .build();

        orders = ordersRepository.save(orders);

        // todo: orderDetailDtos 저장 추가 필요.
        List<OrderDetailDto> orderDetailDtoList = ordersCreateRequestResponseDto.getOrderDetailDtoList();
        List<OrderDetail> orderDetails = new ArrayList<>();

        for(OrderDetailDto orderDetailDto : orderDetailDtoList){
            Optional<Book> optionalBook = bookRepository.findByBookIsbn(orderDetailDto.getBookIsbn());

            if(optionalBook.isEmpty()) {
                throw new BookNotFoundException();
            }

            // 수량 체크
            if(optionalBook.get().getBookQuantity() < orderDetailDto.getQuantity()){
                throw new TooManyOrderException(optionalBook.get().getBookIsbn());
            }

            // order detail 선 저장.
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderDetailId(null)
                    .order(orders)
                    .book(optionalBook.get())
                    .amount(orderDetailDto.getQuantity())
                    .build();

            orderDetail = orderDetailRepository.save(orderDetail);

            // wrap 정보 저장
            // wrap 저장 시, 책보다 더 많은 경우 throw Exception
            long wrapAmount = 0L;
            List<OrderDetailDto.WrapDto> wrapInfoDtoList = orderDetailDto.getWraps();


            for(OrderDetailDto.WrapDto wrapInfoDto : wrapInfoDtoList) {
                Optional<Wrap> optionalWrap = wrapRepository.findById(wrapInfoDto.getWrapId());

                if(optionalWrap.isEmpty()) {
                    throw new NotFoundWrapException(wrapInfoDto.getWrapId());
                }

                WrapInfo wrapInfo = WrapInfo.builder()
                        .pk(new WrapInfo.Pk(optionalWrap.get().getWrapId(), orderDetail.getOrderDetailId()))
                        .wrap(optionalWrap.get())
                        .orderDetail(orderDetail)
                        .amount(wrapInfoDto.getQuantity())
                        .build();

                wrapAmount += wrapInfoDto.getQuantity();
                wrapInfoRepository.save(wrapInfo);
            }

            if(wrapAmount > orderDetail.getAmount()) {
                throw new TooManyWrapForAmountException();
            }

            // 수량 업데이트
            int bookQuantity = (int) (optionalBook.get().getBookQuantity() - orderDetail.getAmount());
            Book book = optionalBook.get().setBookQuantity(bookQuantity);

            // 수량이 0이면 상태 변경.
            if(book.getBookQuantity() == 0) {
                book = book.setBookStatus(1);
            }

            bookRepository.save(book);

            // 쿠폰 업데이트
            if(orderDetailDto.getCouponId() != 0L) {
                Optional<CouponMember> optionalCouponMember = couponMemberRepository.findById(orderDetailDto.getCouponId());

                if (optionalCouponMember.isEmpty()) {
                    throw new NotFoundCouponException(orderDetailDto.getCouponId());
                }

                CouponMember couponMember = optionalCouponMember.get();
                couponMember.setStatus(CouponMember.Status.USED);
                couponMember.setUsedAtToNow();

                couponMemberRepository.save(couponMember);
            }

            // 주문 상세에 추가.
            orderDetails.add(orderDetail);
        }

        // 회원인 경우, 포인트 이력 추가 및 등급 업데이트
        if(ordersCreateRequestResponseDto.getCustomerNo() != null) {
            // 회원 가져오기
            Optional<Member> optionalMember = memberRepository.findById(ordersCreateRequestResponseDto.getCustomerNo());

            if(optionalMember.isEmpty()) {
                throw new MemberNotFoundException();
            }

            PointLog pointLog = PointLog.builder()
                    .pointId(null)
                    .member(optionalMember.get())
                    .orderId(orders.getOrderId())
                    .pointDescription("포인트 적립(상품구매 " + optionalMember.get().getGrade().getGradeNameKor() + " 등급 적립)")
                    .pointUsage((int) ((orders.getTotalFee() * optionalMember.get().getGrade().getAccumulateRate()) / 100))
                    .createdAt(orders.getOrderDate())
                    .build();

            pointLogRepository.save(pointLog);


            PointLog usedPointLog = PointLog.builder()
                    .pointId(null)
                    .member(optionalMember.get())
                    .orderId(orders.getOrderId())
                    .pointDescription("포인트 사용")
                    .pointUsage(-ordersCreateRequestResponseDto.getUsedPoint().intValue())
                    .createdAt(orders.getOrderDate())
                    .build();

            pointLogRepository.save(usedPointLog);

            // 멤버등급 업 조건 만족 시
            List<Grade> grades = gradeRespository.findAll().stream()
                    .sorted((a, b) -> (int) (a.getGradeId() - b.getGradeId()))
                    .collect(Collectors.toList());

            Long userPayment = ordersRepository.findByCustomer(optionalMember.get().getCustomer()).stream()
                    .mapToLong(order -> order.getTotalFee())
                    .sum();
            Long gradeId = optionalMember.get().getGrade().getGradeId();

            for(int i = 0; i < grades.size() - 1; i++) {
                if(grades.get(i).getRequiredPayment() <= userPayment && userPayment < grades.get(i + 1).getRequiredPayment()) {
                    gradeId = grades.get(i).getGradeId();
                }
            }

            Grade grade = gradeRepository.findById(gradeId).get();

            Member member = optionalMember.get();
            member.setGrade(grade);

            memberRepository.save(member);
        }

        // orderdetails를 넣은 후 다시 재저장.
        ordersRepository.save(orders.setOrderDetails(orderDetails));

        return ordersCreateRequestResponseDto;
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
            // bookIsbn 검색
            String bookIsbn = requestBookInfo.getBookIsbn();
            Optional<Book> optionalBook = bookRepository.findByBookIsbn(bookIsbn);

            String bookTitle;

            if (optionalBook.isPresent()) {
                bookTitle = optionalBook.get().getBookTitle();
            } else {
                throw new BookNotFoundException();
            }
            Pageable pageable = PageRequest.of(0, 100);
            Long customerNo = optionalCustomer.get().getCustomerNo();
            List<Long> categoryIds = bookCategoryRepository.findByBook(optionalBook.get()).stream()
                    .map(bookCategory -> bookCategory.getCategory().getCategoryId())
                    .collect(Collectors.toList());

            Page<CouponMember> couponMembers = couponMemberRepository.findCouponMembersByMember_CustomerNo(customerNo, pageable);
            List<CouponMember> couponMemberDtoList = couponMembers.getContent();

            List<CouponMemberResponseDto> couponMemberResponseDtoList  = couponMemberDtoList.stream()
                    .filter(couponMember -> couponMember.getStatus() == CouponMember.Status.ACTIVE)
                    .map(couponMember -> {
                                Long couponId = couponMember.getCoupon().getCouponId();
                                Optional<CouponResponseDto> optionalCouponResponseDto = couponRepository.findCouponById(couponId);

                                if(optionalCouponResponseDto.isEmpty()) {
                                    throw new NotFoundCouponException(couponId);
                                }
                                CouponResponseDto couponResponseDto = optionalCouponResponseDto.get();

                                return CouponMemberResponseDto.buildDto(couponMember, couponResponseDto);
                            }
                    ).collect(Collectors.toList());

            couponMemberResponseDtoList = couponMemberResponseDtoList.stream()
                    .filter(couponMemberResponseDto ->
                        couponMemberResponseDto.getCouponTarget() == Coupon.CouponTarget.NORMAL ||
                        categoryIds.contains(couponMemberResponseDto.getCategoryId()) ||
                        (couponMemberResponseDto.getBookIsbn() != null && couponMemberResponseDto.getBookIsbn().equals(bookIsbn))) // text 비교 수정
                    .collect(Collectors.toList());

            List<Wrap> wraps = optionalBook.get().isBookIsPacking() ? wrapRepository.findAll() : new ArrayList<>();
            totalPrice += requestBookInfo.getBookSalePrice();

            CartPaymentResponseDto.BookInfo responseBookInfo = CartPaymentResponseDto.BookInfo.builder()
                    .bookIsbn(bookIsbn)
                    .bookTitle(bookTitle)
                    .bookSalePrice(requestBookInfo.getBookSalePrice())
                    .quantity(requestBookInfo.getQuantity())
                    .coupons(couponMemberResponseDtoList)
                    .wraps(wraps)
                    .build();

            list.add(responseBookInfo);
        }

        return CartPaymentResponseDto.builder()
                .customerNo(optionalCustomer.get().getCustomerNo())
                .customerName(optionalCustomer.get().getCustomerName())
                .customerPhoneNumber(optionalCustomer.get().getCustomerPhoneNumber())
                .customerEmail(optionalCustomer.get().getCustomerEmail())
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
}
