package com.nhnacademy.shop.coupon.service;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon.exception.IllegalFormCouponRequestException;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.repository.*;
import com.nhnacademy.shop.coupon.service.impl.CouponServiceImpl;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/18
 */
class CouponServiceTest {

    @Mock
    private AmountCouponRepository amountCouponRepository;

    @Mock
    private BookCouponRepository bookCouponRepository;

    @Mock
    private CategoryCouponRepository categoryCouponRepository;

    @Mock
    private PercentageCouponRepository percentageCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CouponMemberRepository couponMemberRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCouponsTest() {
        // Given
        Page<CouponResponseDto> expectedPage = Page.empty();
        when(couponRepository.findAllCoupons(any(PageRequest.class))).thenReturn(expectedPage);

        // When
        Page<CouponResponseDto> resultPage = couponService.getAllCoupons(1, 10);

        // Then
        assertSame(expectedPage, resultPage);
        verify(couponRepository, times(1)).findAllCoupons(any(PageRequest.class));
    }

    @Test
    void getBookCouponsByBookIsbnTest() {
        // Given
        String bookIsbn = "1234567890";
        Page<CouponResponseDto> expectedPage = Page.empty();
        when(couponRepository.findBookCoupons(bookIsbn, PageRequest.of(1, 10))).thenReturn(expectedPage);

        // When
        Page<CouponResponseDto> resultPage = couponService.getBookCoupons(bookIsbn, 1, 10);

        // Then
        assertSame(expectedPage, resultPage);
        verify(couponRepository, times(1)).findBookCoupons(bookIsbn, PageRequest.of(1, 10));
    }

    @Test
    void getCategoryCouponsByCategoryIdTest() {
        // Given
        Long categoryId = 1L;
        Page<CouponResponseDto> expectedPage = Page.empty();
        when(couponRepository.findCategoryCoupons(categoryId, PageRequest.of(1, 10))).thenReturn(expectedPage);

        // When
        Page<CouponResponseDto> resultPage = couponService.getCategoryCoupons(categoryId, 1, 10);

        // Then
        assertSame(expectedPage, resultPage);
        verify(couponRepository, times(1)).findCategoryCoupons(categoryId, PageRequest.of(1, 10));
    }

    @Test
    void getCouponByIdTest() {
        // Given
        Long couponId = 1L;
        CouponResponseDto expectedDto = new CouponResponseDto();
        when(couponRepository.findCouponById(couponId)).thenReturn(Optional.of(expectedDto));

        // When
        CouponResponseDto resultDto = couponService.getCouponById(couponId);

        // Then
        assertSame(expectedDto, resultDto);
        verify(couponRepository, times(1)).findCouponById(couponId);
    }

    @Test
    void getCouponsByContainingNameTest() {
        // Given
        String couponName = "TestCoupon";
        Page<CouponResponseDto> expectedPage = Page.empty();
        when(couponRepository.findCouponsByContainingName(couponName, PageRequest.of(1, 10))).thenReturn(expectedPage);

        // When
        Page<CouponResponseDto> resultPage = couponService.getCouponsByContainingName(couponName, 1, 10);

        // Then
        assertSame(expectedPage, resultPage);
        verify(couponRepository, times(1)).findCouponsByContainingName(couponName, PageRequest.of(1, 10));
    }

    @Test
    void saveBookAmountCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("TestCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.AMOUNT);
        couponDto.setCouponTarget(Coupon.CouponTarget.BOOK);
        couponDto.setDiscountPrice(1000L);
        couponDto.setBookIsbn("1234567890");

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("TestCoupon")
                .deadline(couponDto.getDeadline())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();

        Book book = Book.builder()
                .bookIsbn("ABC123")
                .bookTitle("The Little Prince")
                .bookDesc("hi")
                .bookPublisher("생택쥐페리")
                .bookPublishedAt(LocalDate.now())
                .bookFixedPrice(10000L)
                .bookSalePrice(10000L)
                .bookIsPacking(true)
                .bookViews(203L)
                .bookStatus(1)
                .bookQuantity(15)
                .bookImage("img.png")
                .build();

        when(bookRepository.findByBookIsbn(any())).thenReturn(Optional.ofNullable(book));
        when(couponRepository.save(any())).thenReturn(savedCoupon);


        // When
        CouponResponseDto resultDto = couponService.saveCoupon(couponDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(savedCoupon.getCouponName(), resultDto.getCouponName());
        assertEquals(savedCoupon.getDeadline(), resultDto.getDeadline());
        assertEquals(savedCoupon.getCouponStatus(), resultDto.getCouponStatus());
        assertEquals(savedCoupon.getCouponType(), resultDto.getCouponType());
        assertEquals(savedCoupon.getCouponTarget(), resultDto.getCouponTarget());
        verify(couponRepository, times(1)).save(any());
    }
    @Test
    void saveBookPercentageCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("BookPercentageCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.PERCENTAGE);
        couponDto.setCouponTarget(Coupon.CouponTarget.BOOK);
        couponDto.setDiscountRate(10L);
        couponDto.setMaxDiscountPrice(5000L);
        couponDto.setBookIsbn("1234567890");

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("BookPercentageCoupon")
                .deadline(LocalDate.now())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();

        Book book = Book.builder()
                .bookIsbn("ABC123")
                .bookTitle("The Little Prince")
                .bookDesc("hi")
                .bookPublisher("생택쥐페리")
                .bookPublishedAt(LocalDate.now())
                .bookFixedPrice(10000L)
                .bookSalePrice(10000L)
                .bookIsPacking(true)
                .bookViews(203L)
                .bookStatus(1)
                .bookQuantity(15)
                .bookImage("img.png")
                .build();

        when(bookRepository.findByBookIsbn(any())).thenReturn(Optional.ofNullable(book));
        when(couponRepository.save(any())).thenReturn(savedCoupon);

        // When
        CouponResponseDto resultDto = couponService.saveCoupon(couponDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(savedCoupon.getCouponName(), resultDto.getCouponName());
        assertEquals(savedCoupon.getDeadline(), resultDto.getDeadline());
        assertEquals(savedCoupon.getCouponStatus(), resultDto.getCouponStatus());
        assertEquals(savedCoupon.getCouponType(), resultDto.getCouponType());
        assertEquals(savedCoupon.getCouponTarget(), resultDto.getCouponTarget());
        verify(couponRepository, times(1)).save(any());
    }


    @Test
    void saveNormalAmountCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("NormalAmountCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.AMOUNT);
        couponDto.setCouponTarget(Coupon.CouponTarget.NORMAL);
        couponDto.setDiscountPrice(1000L);

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("NormalAmountCoupon")
                .deadline(LocalDate.now())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.NORMAL)
                .build();
        when(couponRepository.save(any())).thenReturn(savedCoupon);

        // When
        CouponResponseDto resultDto = couponService.saveCoupon(couponDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(savedCoupon.getCouponName(), resultDto.getCouponName());
        assertEquals(savedCoupon.getDeadline(), resultDto.getDeadline());
        assertEquals(savedCoupon.getCouponStatus(), resultDto.getCouponStatus());
        assertEquals(savedCoupon.getCouponType(), resultDto.getCouponType());
        assertEquals(savedCoupon.getCouponTarget(), resultDto.getCouponTarget());
        verify(couponRepository, times(1)).save(any());
    }

    @Test
    void saveNormalPercentageCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("NormalPercentageCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.PERCENTAGE);
        couponDto.setCouponTarget(Coupon.CouponTarget.NORMAL);
        couponDto.setDiscountRate(10L);
        couponDto.setMaxDiscountPrice(5000L);

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("NormalPercentageCoupon")
                .deadline(LocalDate.now())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.NORMAL)
                .build();
        when(couponRepository.save(any())).thenReturn(savedCoupon);

        // When
        CouponResponseDto resultDto = couponService.saveCoupon(couponDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(savedCoupon.getCouponName(), resultDto.getCouponName());
        assertEquals(savedCoupon.getDeadline(), resultDto.getDeadline());
        assertEquals(savedCoupon.getCouponStatus(), resultDto.getCouponStatus());
        assertEquals(savedCoupon.getCouponType(), resultDto.getCouponType());
        assertEquals(savedCoupon.getCouponTarget(), resultDto.getCouponTarget());
        verify(couponRepository, times(1)).save(any());
    }

    @Test
    void saveCategoryAmountCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("CategoryAmountCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.AMOUNT);
        couponDto.setCouponTarget(Coupon.CouponTarget.CATEGORY);
        couponDto.setDiscountPrice(1000L);
        couponDto.setCategoryId(1L);

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("CategoryAmountCoupon")
                .deadline(LocalDate.now())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.CATEGORY)
                .build();

        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("판타지")
                .parentCategory(null).build();

        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        when(couponRepository.save(any())).thenReturn(savedCoupon);

        // When
        CouponResponseDto resultDto = couponService.saveCoupon(couponDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(savedCoupon.getCouponName(), resultDto.getCouponName());
        assertEquals(savedCoupon.getDeadline(), resultDto.getDeadline());
        assertEquals(savedCoupon.getCouponStatus(), resultDto.getCouponStatus());
        assertEquals(savedCoupon.getCouponType(), resultDto.getCouponType());
        assertEquals(savedCoupon.getCouponTarget(), resultDto.getCouponTarget());
        verify(couponRepository, times(1)).save(any());
    }

    @Test
    void saveCategoryPercentCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("CategoryPercentageCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.PERCENTAGE);
        couponDto.setCouponTarget(Coupon.CouponTarget.CATEGORY);
        couponDto.setDiscountRate(10L);
        couponDto.setMaxDiscountPrice(5000L);
        couponDto.setCategoryId(1L);

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("CategoryPercentageCoupon")
                .deadline(LocalDate.now())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.CATEGORY)
                .build();

        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("판타지")
                .parentCategory(null).build();

        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        when(couponRepository.save(any())).thenReturn(savedCoupon);

        // When
        CouponResponseDto resultDto = couponService.saveCoupon(couponDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(savedCoupon.getCouponName(), resultDto.getCouponName());
        assertEquals(savedCoupon.getDeadline(), resultDto.getDeadline());
        assertEquals(savedCoupon.getCouponStatus(), resultDto.getCouponStatus());
        assertEquals(savedCoupon.getCouponType(), resultDto.getCouponType());
        assertEquals(savedCoupon.getCouponTarget(), resultDto.getCouponTarget());
        verify(couponRepository, times(1)).save(any());
    }


    @Test
    void saveInvalidCategoryPercentCouponTest() {
        // Given
        CouponRequestDto couponDto = new CouponRequestDto();
        couponDto.setCouponName("CategoryPercentageCoupon");
        LocalDate deadline = LocalDate.now().plusDays(7);
        couponDto.setDeadline(LocalDate.now());
        couponDto.setCouponStatus(Coupon.Status.ACTIVE);
        couponDto.setCouponType(Coupon.CouponType.PERCENTAGE);
        couponDto.setCouponTarget(Coupon.CouponTarget.CATEGORY);
        couponDto.setMaxDiscountPrice(5000L);
        couponDto.setCategoryId(1L);

        Coupon savedCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("CategoryPercentageCoupon")
                .deadline(LocalDate.now())
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.CATEGORY)
                .build();

        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("판타지")
                .parentCategory(null).build();

        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(category));
        when(couponRepository.save(any())).thenReturn(savedCoupon);

        // When
        try {
            couponService.saveCoupon(couponDto);
        } catch (Exception e) {

        }

        // Then
        assertThrows(IllegalFormCouponRequestException.class, () -> couponService.saveCoupon(couponDto));
    }

    @Test
    void deleteInvalidIdCoupon() {
        // Given
        Long couponId = 1L;
        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotFoundCouponException.class, () -> couponService.deleteCoupon(couponId));
    }

    @Test
    void deleteAmountCoupon() {
        // Given
        Long couponId = 1L;
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(new Coupon()));
        when(amountCouponRepository.existsById(couponId)).thenReturn(true);

        // When
        couponService.deleteCoupon(couponId);

        // Then
        verify(couponRepository, times(1)).findById(couponId);
        verify(amountCouponRepository, times(1)).deleteById(couponId);
        verify(couponRepository, times(1)).deleteById(couponId);
    }

    @Test
    void deletePercentageCoupon() {
        // Given
        Long couponId = 1L;
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(new Coupon()));
        when(percentageCouponRepository.existsById(couponId)).thenReturn(true);

        // When
        couponService.deleteCoupon(couponId);

        // Then
        verify(couponRepository, times(1)).findById(couponId);
        verify(percentageCouponRepository, times(1)).deleteById(couponId);
        verify(couponRepository, times(1)).deleteById(couponId);
    }

    @Test
    void deleteBookCoupon() {
        // Given
        Long couponId = 1L;
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(new Coupon()));
        when(bookCouponRepository.existsById(couponId)).thenReturn(true);

        // When
        couponService.deleteCoupon(couponId);

        // Then
        verify(couponRepository, times(1)).findById(couponId);
        verify(bookCouponRepository, times(1)).deleteById(couponId);
        verify(couponRepository, times(1)).deleteById(couponId);
    }

    @Test
    void deleteCategoryCoupon() {
        // Given
        Long couponId = 1L;
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(new Coupon()));
        when(categoryCouponRepository.existsById(couponId)).thenReturn(true);

        // When
        couponService.deleteCoupon(couponId);

        // Then
        verify(couponRepository, times(1)).findById(couponId);
        verify(categoryCouponRepository, times(1)).deleteById(couponId);
        verify(couponRepository, times(1)).deleteById(couponId);
    }

    @Test
    void getAllAvailableCoupons() {
        // coupon initialize
        Coupon normalAmountCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("normalAmountCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.NORMAL)
                .build();
        Coupon normalPercentageCoupon = Coupon.builder()
                .couponId(2L)
                .couponName("normalPercentageCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.NORMAL)
                .build();
        Coupon categoryAmountCoupon = Coupon.builder()
                .couponId(3L)
                .couponName("categoryAmountCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.CATEGORY)
                .build();
        Coupon categoryPercentageCoupon = Coupon.builder()
                .couponId(4L)
                .couponName("categoryPercentageCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.CATEGORY)
                .build();
        Coupon bookAmountCoupon = Coupon.builder()
                .couponId(5L)
                .couponName("bookAmountCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();
        Coupon bookPercentageCoupon = Coupon.builder()
                .couponId(6L)
                .couponName("bookPercentageCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();
        Coupon notIssuedBookPercentageCoupon = Coupon.builder()
                .couponId(7L)
                .couponName("bookPercentageCoupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.PERCENTAGE)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();

        // member initialize
        Customer customer = Customer.builder()
                .customerNo(1L)
                .customerId("123")
                .customerPassword("password")
                .customerName("name")
                .customerPhoneNumber("010-1234-2345")
                .customerEmail("name@naver.com")
                .customerBirthday(LocalDate.of(2000, 11, 30))
                .customerRole("GUEST")
                .build();

        Grade grade = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .accumulateRate(5L)
                .build();

        Member member = Member.builder()
                .memberId("123")
                .customer(customer)
                .lastLoginAt(LocalDateTime.now())
                .grade(grade)
                .role("ROLE_ADMIN")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        Category category = Category.builder()
                .categoryId(1L)
                .categoryName("categoryName")
                .parentCategory(null)
                .build();

        Book book = Book.builder()
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

        AmountCoupon amountCoupon = AmountCoupon.builder()
                .coupon(bookAmountCoupon)
                .discountPrice(2000L)
                .build();
        PercentageCoupon percentageCoupon = PercentageCoupon.builder()
                .coupon(bookPercentageCoupon)
                .maxDiscountPrice(2000L)
                .discountRate(5L)
                .build();
        BookCoupon bookCoupon = BookCoupon.builder()
                .coupon(bookAmountCoupon)
                .book(book)
                .build();
        CategoryCoupon categoryCoupon = CategoryCoupon.builder()
                .coupon(categoryAmountCoupon)
                .category(category)
                .build();

        // all coupon save
        when(couponRepository.findAll()).thenReturn(
                List.of(normalAmountCoupon, normalPercentageCoupon,
                        categoryAmountCoupon, categoryPercentageCoupon,
                        bookAmountCoupon, bookPercentageCoupon,
                        notIssuedBookPercentageCoupon)
        );

        // target coupon save
        when(amountCouponRepository.findById(1L)).thenReturn(Optional.of(amountCoupon));
        when(amountCouponRepository.findById(3L)).thenReturn(Optional.of(amountCoupon));
        when(amountCouponRepository.findById(5L)).thenReturn(Optional.of(amountCoupon));
        when(percentageCouponRepository.findById(2L)).thenReturn(Optional.of(percentageCoupon));
        when(percentageCouponRepository.findById(4L)).thenReturn(Optional.of(percentageCoupon));
        when(percentageCouponRepository.findById(6L)).thenReturn(Optional.of(percentageCoupon));
        when(percentageCouponRepository.findById(7L)).thenReturn(Optional.of(percentageCoupon));

        // type coupon save
        when(categoryCouponRepository.findById(3L)).thenReturn(Optional.of(categoryCoupon));
        when(categoryCouponRepository.findById(4L)).thenReturn(Optional.of(categoryCoupon));
        when(bookCouponRepository.findById(5L)).thenReturn(Optional.of(bookCoupon));
        when(bookCouponRepository.findById(6L)).thenReturn(Optional.of(bookCoupon));
        when(bookCouponRepository.findById(7L)).thenReturn(Optional.of(bookCoupon));


        // user coupon save
        when(couponMemberRepository.findCouponMembersByMember_CustomerNo(1L)).thenReturn(
                List.of(
                        CouponMember.builder()
                                .couponMemberId(1L)
                                .coupon(bookPercentageCoupon)
                                .member(member)
                                .used(false)
                                .createdAt(LocalDateTime.now())
                                .usedAt(LocalDateTime.now())
                                .status(CouponMember.Status.ACTIVE)
                                .build(),
                        CouponMember.builder()
                                .couponMemberId(1L)
                                .coupon(bookPercentageCoupon)
                                .member(member)
                                .used(false)
                                .createdAt(LocalDateTime.now())
                                .usedAt(LocalDateTime.now())
                                .status(CouponMember.Status.ACTIVE)
                                .build()
                )
        );

        assertEquals(couponService.getAllAvailableCoupons(1L, 1, 10).getContent().size(), 6);
    }
}