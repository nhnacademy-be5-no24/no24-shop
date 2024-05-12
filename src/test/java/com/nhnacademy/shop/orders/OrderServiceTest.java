package com.nhnacademy.shop.orders;


import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.category.service.CategoryService;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.coupon.repository.CategoryCouponRepository;
import com.nhnacademy.shop.coupon.repository.CouponRepository;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.member.domain.Member;
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
import com.nhnacademy.shop.orders.exception.SaveOrderFailed;
import com.nhnacademy.shop.orders.repository.OrdersRepository;
import com.nhnacademy.shop.orders.service.impl.OrdersServiceImpl;
import com.nhnacademy.shop.payment.domain.Payment;
import com.nhnacademy.shop.payment.exception.PaymentNotFoundException;
import com.nhnacademy.shop.payment.repository.PaymentRepository;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.repository.PointLogRepository;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import com.nhnacademy.shop.wrap.repository.WrapInfoRepository;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
@WebAppConfiguration
class OrderServiceTest {
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CouponMemberRepository couponMemberRepository;
    @Mock
    private WrapRepository wrapRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private WrapInfoRepository wrapInfoRepository;
    @Mock
    private PointLogRepository pointLogRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private GradeRepository gradeRespository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private CategoryCouponRepository categoryCouponRepository;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private EntityManager entityManager;
    private Customer customer;
    private Book book;
    private Wrap wrap;
    private Orders order;
    private OrderDetail orderDetail;
    private Payment payment;
    private WrapInfo wrapInfo;
    private Address address;
    private Member member;
    private Grade grade;
    private BookCategory bookCategory;
    private Category category;
    private Coupon coupon;
    private PointLog point;
    private CouponMember couponMember;
    private OrdersListForAdminResponseDto adminResponseDto;
    private OrdersListForAdminResponseDto adminResponseDto2;
    private OrdersResponseDto ordersResponseDto;
    private OrdersResponseDto ordersResponseDto2;
    private OrdersCreateRequestResponseDto createRequestDto;
    private OrdersCreateRequestResponseDto createRequestDtoNoState;
    private CartPaymentRequestDto cartPaymentRequestDto;
    private CouponResponseDto couponResponseDto;

    @BeforeEach
    void setup() {
        initializeEntities();
        initializeResponseDto();
    }
    private void initializeEntities() {
        payment = Payment.builder()
                .paymentId(1L).paymentName("name").build();

        customer = Customer.builder()
                .customerNo(1L)
                .customerId("id")
                .customerPassword("password")
                .customerName("name")
                .customerPhoneNumber("phoneNumber")
                .customerEmail("email")
                .customerBirthday(LocalDate.of(2024, 4, 14))
                .customerRole("role")
                .build();

        book = Book.builder()
                .bookIsbn("bookIsbn")
                .bookTitle("bookTitle")
                .bookDesc("desc")
                .bookPublisher("publisher")
                .bookPublishedAt(LocalDate.of(2024, 4, 14))
                .bookFixedPrice(1L)
                .bookSalePrice(1L)
                .bookIsPacking(true)
                .bookViews(1L)
                .bookStatus(0)
                .bookQuantity(1)
                .bookImage("image")
                .build();

        wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("name")
                .wrapCost(1L)
                .build();

        order = Orders.builder()
                .orderId("orderId")
                .orderDate(LocalDateTime.of(2024, 4, 14, 20, 10, 29))
                .shipDate(LocalDate.now())
                .deliveryFee(1)
                .totalFee(1L)
                .orderState(Orders.OrderState.WAITING)
                .payment(payment)
                .customer(customer)
                .receiverName("name")
                .receiverPhoneNumber("number")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("addressDetail")
                .build();

        orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .book(book)
                .order(order)
                .amount(1L)
                .build();

        wrapInfo = WrapInfo.builder()
                .pk(new WrapInfo.Pk(wrap.getWrapId(), orderDetail.getOrderDetailId()))
                .wrap(wrap)
                .orderDetail(orderDetail)
                .amount(2L)
                .build();
        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("gradeName")
                .accumulateRate(1L)
                .build();
        member = Member.builder()
                .customerNo(1L)
                .customer(customer)
                .memberId("memberId")
                .lastLoginAt(LocalDateTime.now())
                .grade(grade)
                .role("role")
                .memberState(Member.MemberState.ACTIVE)
                .build();
        address = Address.builder()
                .addressId(1L)
                .alias("alias")
                .receiverName("name")
                .receiverPhoneNumber("phoneNumber")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("addressDetail")
                .isDefault(Boolean.TRUE)
                .member(member)
                .build();
        category = Category.builder()
                .categoryId(1L)
                .categoryName("categoryName")
                .parentCategory(null)
                .build();
        bookCategory = BookCategory.builder()
                .pk(new BookCategory.Pk("bookIsbn", 1L))
                .book(book)
                .category(category)
                .build();
        coupon = Coupon.builder()
                .couponId(1L)
                .couponName("couponName")
                .deadline(LocalDate.now())
                .issueLimit(1)
                .expirationPeriod(1)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();
        couponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(Boolean.FALSE)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now())
                .usedAt(null)
                .status(CouponMember.Status.ACTIVE)
                .build();
        point = PointLog.builder()
                .pointId(1L)
                .member(member)
                .orderId("orderId")
                .pointDescription("description")
                .pointUsage(1)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private void initializeResponseDto() {

        OrderDetailDto.WrapDto wrapDto = OrderDetailDto.WrapDto.builder()
                .wrapId(1L)
                .quantity(1L)
                .build();
        OrderDetailDto orderDetailDto = OrderDetailDto.builder()
                .bookIsbn(book.getBookIsbn())
                .quantity(1L)
                .price(1L)
                .couponId(1L)
                .wraps(List.of(wrapDto))
                .build();
        List<OrderDetailDto> orderDetailDtoList = List.of(orderDetailDto);
        createRequestDto = OrdersCreateRequestResponseDto.builder()
                .orderId(order.getOrderId())
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.COMPLETE_PAYMENT)
                .totalFee(1L)
                .deliveryFee(1)
                .paymentId(payment.getPaymentId())
                .customerNo(customer.getCustomerNo())
                .jSessionId("jSessionId")
                .receiverName("receiverName")
                .receiverPhoneNumber("phoneNumber")
                .zipcode("zipcode")
                .address(order.getAddress())
                .addressDetail(order.getAddressDetail())
                .req(order.getReq())
                .usedPoint(1L)
                .orderDetailDtoList(orderDetailDtoList)
                .build();


        adminResponseDto = OrdersListForAdminResponseDto.builder()
                .orderId("orderId")
                .customerName("customerName")
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.WAITING)
                .address("address")
                .addressDetail("addressDetail")
                .wrapName("wrapName")
                .wrapCost(1L)
                .bookTitle("bookTittle")
                .bookSalePrice(1L).build();
        adminResponseDto2 = OrdersListForAdminResponseDto.builder()
                .orderId("orderId2")
                .customerName("customerName2")
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.WAITING)
                .address("address")
                .addressDetail("addressDetail")
                .wrapName("wrapName")
                .wrapCost(1L)
                .bookTitle("bookTittle")
                .bookSalePrice(1L).build();

         ordersResponseDto = new OrdersResponseDto("orderId",
                "bookTitle",
                1L,
                "wrapName",
                1L,
                LocalDateTime.now(),
                "receiverName",
                "phoneNumber",
                "address",
                "addressDetail",
                Orders.OrderState.WAITING);
        ordersResponseDto2 = new OrdersResponseDto("orderId2",
                "bookTitle",
                1L,
                "wrapName",
                1L,
                LocalDateTime.now(),
                "receiverName",
                "phoneNumber",
                "address",
                "addressDetail",
                Orders.OrderState.COMPLETED);

        couponResponseDto = CouponResponseDto.builder()
                .couponId(1L)
                .couponName("couponName")
                .issueLimit(1)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .bookIsbn("bookIsbn")
                .categoryId(1L)
                .discountPrice(1L)
                .discountRate(null)
                .maxDiscountPrice(2L)
                .build();

        CartPaymentRequestDto.BookInfo bookInfo = CartPaymentRequestDto.BookInfo.builder()
                .bookIsbn("bookIsbn")
                .bookSalePrice(1L)
                .quantity(1L)
                .build();
        List<CartPaymentRequestDto.BookInfo> bookInfoList = List.of(bookInfo);
        cartPaymentRequestDto = CartPaymentRequestDto.builder()
                .bookInfos(bookInfoList)
                .customerNo(1L)
                .build();

    }

//    @AfterEach
//    public void teardown() {
//        this.entityManager.createNativeQuery("ALTER TABLE customer ALTER COLUMN `customer_no` RESTART WITH 1").executeUpdate();
//        this.entityManager.createNativeQuery("ALTER TABLE payment ALTER COLUMN `payment_id` RESTART WITH 1").executeUpdate();
//        this.entityManager.createNativeQuery("ALTER TABLE wrap ALTER COLUMN `wrap_id` RESTART WITH 1").executeUpdate();
//        this.entityManager.createNativeQuery("ALTER TABLE order_detail ALTER COLUMN `order_detail_id` RESTART WITH 1").executeUpdate();
//    }
    @Test
    @DisplayName("주문리스트 전체 가져오기 test")
    void testGetOrders() {
        List<OrdersListForAdminResponseDto> dummyData = new ArrayList<>();
        dummyData.add(new OrdersListForAdminResponseDto("orderId1", "customerName1", LocalDateTime.now(), LocalDate.now(), Orders.OrderState.WAITING, "address1", "addressDetail1", "wrapName1", 1L, "bookTitle1", 1L));
        dummyData.add(new OrdersListForAdminResponseDto("orderId2", "customerName2", LocalDateTime.now(), LocalDate.now(), Orders.OrderState.WAITING, "address2", "addressDetail2", "wrapName2", 2L, "bookTitle2", 2L));
        Page<OrdersListForAdminResponseDto> dummyPage = new PageImpl<>(dummyData);

        // Mock 객체에 대한 행동 설정
        when(ordersRepository.getOrderList(any(Pageable.class))).thenReturn(dummyPage);

        // 주문 리스트 가져오기
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 페이지당 10개씩
        Page<OrdersListForAdminResponseDto> resultPage = ordersService.getOrders(pageable);

        // 결과 검증
        assertEquals(dummyPage.getTotalElements(), resultPage.getTotalElements());
        assertEquals(dummyPage.getContent().size(), resultPage.getContent().size());
    }

    @Test
    @DisplayName("주문아이디로 상품리스트 가져오기 test")
    void testGetOrderByOrdersId() {
//        when(ordersRepository.getOrderByOrderId(anyString())).thenReturn(Optional.of(ordersResponseDto));
//
//        OrdersResponseDto result = ordersService.getOrderByOrdersId("orderId");
//
//        verify(ordersRepository, times(1)).getOrderByOrderId("orderId");
//        assertEquals(ordersResponseDto, result);
//        assertEquals("orderId", ordersResponseDto.getOrderId());
        String orderId = "orderId";

        // 주문 객체 생성
        Orders order = Orders.builder()
                .orderId(orderId)
                .orderDate(LocalDateTime.now())
                .receiverName("receiverName")
                .receiverPhoneNumber("receiverPhoneNumber")
                .address("address")
                .addressDetail("addressDetail")
                .orderState(Orders.OrderState.WAITING)
                .totalFee(10000L)
                .build();

        // Mock 객체에 대한 행동 설정
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        // 주문 상세 정보 가져오기
        OrdersResponseDto resultDto = ordersService.getOrderByOrdersId(orderId);

        // 결과 검증
        assertNotNull(resultDto);
        assertEquals(orderId, resultDto.getOrderId());
        assertEquals(order.getOrderDate(), resultDto.getOrderDate());
        assertEquals(order.getReceiverName(), resultDto.getReceiverName());
        assertEquals(order.getReceiverPhoneNumber(), resultDto.getReceiverPhoneNumber());
        assertEquals(order.getAddress(), resultDto.getAddress());
        assertEquals(order.getAddressDetail(), resultDto.getAddressDetail());
        assertEquals(order.getOrderState(), resultDto.getOrderState());
        assertEquals(order.getTotalFee(), resultDto.getTotalPrice());
    }

    @Test
    @DisplayName("고객번호로 상품리스트 가져오기 test")
    void testGetOrderByCustomer() {

//        List<OrdersResponseDto> mockedResponse = Arrays.asList(ordersResponseDto, ordersResponseDto2);
//        Page<OrdersResponseDto> mockedPage = new PageImpl<>(mockedResponse);
//        when(ordersRepository.getOrderListByCustomer(any(Pageable.class), eq(1L))).thenReturn(mockedPage);
//
//        Page<OrdersResponseDto> result = ordersService.getOrderByCustomer(PageRequest.of(0, 10), 1L);
//
//        verify(ordersRepository, times(1)).getOrderListByCustomer(any(Pageable.class), eq(1L));
//        assertEquals(mockedPage, result);
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 페이지당 10개씩

        // 고객 정보 생성
        Long customerNo = 1L;
        Customer customer = Customer.builder()
                .customerNo(customerNo)
                .build();

        // 주문 리스트 생성
        List<Orders> ordersList = new ArrayList<>();
        Orders order1 = Orders.builder()
                .orderId("orderId1")
                .orderDate(LocalDateTime.now())
                .receiverName("receiverName1")
                .receiverPhoneNumber("receiverPhoneNumber1")
                .address("address1")
                .addressDetail("addressDetail1")
                .orderState(Orders.OrderState.WAITING)
                .totalFee(10000L)
                .build();
        // ordersList에 다른 주문 정보 추가

        // ordersRepository 행동 설정
        when(customerRepository.findById(customerNo)).thenReturn(Optional.of(customer));
        when(ordersRepository.findByCustomer(customer)).thenReturn(ordersList);

        // 주문 리스트 가져오기
        Page<OrdersResponseDto> resultPage = ordersService.getOrderByCustomer(pageable, customerNo);

        // 결과 검증
        assertNotNull(resultPage);
        assertEquals(0, resultPage.getNumber()); // 현재 페이지 번호가 0인지 확인
        assertEquals(10, resultPage.getSize()); // 페이지 크기가 10인지 확인
        // 나머지 필요한 검증 로직 추가
    }

    @Test
    @DisplayName("주문 생성 - 성공 test ")
    void testCreateOrder_Success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(paymentRepository.findById(createRequestDto.getPaymentId())).thenReturn(Optional.of(payment));
        when(customerRepository.findById(createRequestDto.getCustomerNo())).thenReturn(Optional.of(customer));
        when(ordersRepository.save(any())).thenReturn(order);
        when(bookRepository.findByBookIsbn(anyString())).thenReturn(Optional.of(book));
        when(orderDetailRepository.save(any())).thenReturn(orderDetail);
        when(wrapRepository.findById(anyLong())).thenReturn(Optional.of(wrap));
        when(wrapInfoRepository.save(any())).thenReturn(wrapInfo);
        when(bookRepository.save(any())).thenReturn(book);
        when(couponMemberRepository.findById(anyLong())).thenReturn(Optional.of(couponMember));
        when(pointLogRepository.save(any())).thenReturn(point);
        when(gradeRespository.findById(anyLong())).thenReturn(Optional.of(grade));
        OrdersCreateRequestResponseDto responseDto = ordersService.createOrder(createRequestDto);

        verify(ordersRepository, times(2)).save(any());
        verify(orderDetailRepository, times(1)).save(any());
        verify(wrapInfoRepository, times(1)).save(any());
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문 상태 수정 - 성공 test")
    void testModifyOrderState_OrderExists() {

        String orderId = "orderId";
        Orders.OrderState newState = Orders.OrderState.COMPLETED;

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));

        ordersService.modifyOrderState(orderId, newState);
        ArgumentCaptor<Orders> ordersCaptor = ArgumentCaptor.forClass(Orders.class);
        verify(ordersRepository, times(1)).save(ordersCaptor.capture());
        Orders savedOrder = ordersCaptor.getValue();

        assertEquals(newState, savedOrder.getOrderState());
    }

    @Test
    @DisplayName("주문 상태 수정 - 실패: 주문이 존재하지 않음 test")
    void testModifyOrderState_OrderNotExists() {

        String orderId = "nonExistentOrderId";
        Orders.OrderState newState = Orders.OrderState.COMPLETED;

        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(NotFoundOrderException.class, () -> ordersService.modifyOrderState(orderId, newState));

        verify(ordersRepository, never()).save(any());
    }

    @Test
    @DisplayName("주문결제페이지 정보 만들기")
    void testGetCartPaymentInfo(){
       when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
       when(addressRepository.findByMemberCustomerNo(anyLong())).thenReturn(List.of(address));
       when(bookRepository.findByBookIsbn(anyString())).thenReturn(Optional.of(book));
       when(bookCategoryRepository.findByBook(any())).thenReturn(List.of(bookCategory));
       List<CouponMember> couponMembers = List.of(couponMember);
       when(couponRepository.findCouponById(anyLong())).thenReturn(Optional.of(couponResponseDto));
       Page<CouponMember> couponMemberPage = new PageImpl<>(couponMembers);
       when(couponMemberRepository.findCouponMembersByMember_CustomerNo(anyLong(), any())).thenReturn(couponMemberPage);
       when(wrapRepository.findAll()).thenReturn(List.of(wrap));

       CartPaymentResponseDto cartPaymentResponseDto = ordersService.getCartPaymentInfo(cartPaymentRequestDto);


        verify(customerRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1)).findByMemberCustomerNo(anyLong());
        verify(bookRepository, times(cartPaymentRequestDto.getBookInfos().size())).findByBookIsbn(anyString());
        verify(bookCategoryRepository, times(cartPaymentRequestDto.getBookInfos().size())).findByBook(any());
        verify(couponMemberRepository, times(1)).findCouponMembersByMember_CustomerNo(anyLong(), any());
        verify(wrapRepository, times(cartPaymentRequestDto.getBookInfos().size())).findAll();

        assertEquals(customer.getCustomerNo(), cartPaymentResponseDto.getCustomerNo());
        assertEquals(customer.getCustomerName(), cartPaymentResponseDto.getCustomerName());
        assertEquals(customer.getCustomerPhoneNumber(), cartPaymentResponseDto.getCustomerPhoneNumber());
        assertEquals(customer.getCustomerEmail(), cartPaymentResponseDto.getCustomerEmail());
        assertEquals(address.getReceiverName(), cartPaymentResponseDto.getReceiverName());
        assertEquals(address.getReceiverPhoneNumber(), cartPaymentResponseDto.getReceiverPhoneNumber());
        assertEquals(address.getZipcode(), cartPaymentResponseDto.getZipcode());
        assertEquals(address.getAddress(), cartPaymentResponseDto.getAddress());
        assertEquals(address.getAddressDetail(), cartPaymentResponseDto.getAddressDetail());
        assertEquals("요청사항 입력", cartPaymentResponseDto.getReq());

        assertEquals(cartPaymentRequestDto.getBookInfos().size(), cartPaymentResponseDto.getBookInfos().size());
        long expectedTotalPrice = cartPaymentRequestDto.getBookInfos().stream()
                .mapToLong(bookInfo -> bookInfo.getBookSalePrice() * bookInfo.getQuantity())
                .sum();
        if (expectedTotalPrice < 30000) {
            expectedTotalPrice += 3000;
        }
        assertEquals(expectedTotalPrice, cartPaymentResponseDto.getTotalPrice());

    }

}
