package com.nhnacademy.shop.coupon.repository;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon.repository.impl.CouponRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/18
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class CouponRepositoryTest {
    private Pageable pageable;
    private Coupon coupon;
    private AmountCoupon amountCoupon;
    private PercentageCoupon percentageCoupon;
    private CategoryCoupon categoryCoupon;
    private BookCoupon bookCoupon;
    private Book book;
    private Category category;

    Coupon.Status[] statuses = {
            Coupon.Status.ACTIVE,
            Coupon.Status.DEACTIVATED
    };
    Coupon.CouponType[] types = {
            Coupon.CouponType.AMOUNT,
            Coupon.CouponType.PERCENTAGE
    };
    Coupon.CouponTarget[] targets = {
            Coupon.CouponTarget.NORMAL,
            Coupon.CouponTarget.BOOK,
            Coupon.CouponTarget.CATEGORY
    };

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AmountCouponRepository amountCouponRepository;
    @Autowired
    private PercentageCouponRepository percentageCouponRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookCouponRepository bookCouponRepository;
    @Autowired
    private CategoryCouponRepository categoryCouponRepository;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .categoryId(1L)
                .categoryName("판타지")
                .parentCategory(null).build();

        category = categoryRepository.save(category);

        book = Book.builder()
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

        book = bookRepository.save(book);

        pageable = PageRequest.of(0, 10);

        for(int i = 0; i < statuses.length; i++) {
            for(int j = 0; j < types.length; j++) {
                for(int k = 0; k < targets.length; k++) {
                    long index = (long) (6 * i + 2 * j + k + 1);
                    coupon = Coupon.builder()
                            .couponId(index)
                            .couponName("Coupon " + index)
                            .deadline(LocalDate.now())
                            .couponStatus(statuses[i])
                            .couponType(types[j])
                            .couponTarget(targets[k])
                            .build();

                    coupon = couponRepository.save(coupon);

                    // save TypeCoupon
                    if(types[j] == Coupon.CouponType.AMOUNT) {
                        amountCoupon = AmountCoupon.builder()
                                .coupon(coupon)
                                .discountPrice(1000L)
                                .build();
                        amountCouponRepository.save(amountCoupon);
                    }
                    else {
                        percentageCoupon = PercentageCoupon.builder()
                                .coupon(coupon)
                                .discountRate(5L)
                                .maxDiscountPrice(2000L)
                                .build();
                        percentageCouponRepository.save(percentageCoupon);
                    }

                    // save TargetCoupon
                    if(targets[k] == Coupon.CouponTarget.BOOK) {
                        bookCoupon = BookCoupon.builder()
                                .coupon(coupon)
                                .book(book)
                                .build();
                        BookCoupon b = bookCouponRepository.save(bookCoupon);
                        System.out.println(b);
                    }
                    else if(targets[k] == Coupon.CouponTarget.CATEGORY) {
                        categoryCoupon = CategoryCoupon.builder()
                                .coupon(coupon)
                                .category(category)
                                .build();
                        CategoryCoupon c = categoryCouponRepository.save(categoryCoupon);
                        System.out.println(c);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName(value = "쿠폰 리스트 전체 조회")
    void findAllCouponsTest() {
        pageable = PageRequest.of(0, 10);
        Page<CouponResponseDto> dtoPage = couponRepository.findAllCoupons(pageable);
        List<CouponResponseDto> couponList = dtoPage.getContent();

        assertEquals(10, couponList.size());

        pageable = PageRequest.of(0, 2);
        dtoPage = couponRepository.findAllCoupons(pageable);
        couponList = dtoPage.getContent();

        assertEquals(2, couponList.size());
    }

    @Test
    void findBookCouponsTest() {
        pageable = PageRequest.of(0, 10);
        Page<CouponResponseDto> dtoPage = couponRepository.findBookCoupons("ABC123", pageable);
        List<CouponResponseDto> couponList = dtoPage.getContent();

        assertEquals(4, couponList.size());
    }

    @Test
    void findCategoryCouponsTest() {
        pageable = PageRequest.of(0, 10);
        Page<CouponResponseDto> dtoPage = couponRepository.findCategoryCoupons(1L, pageable);
        List<CouponResponseDto> couponList = dtoPage.getContent();

        assertEquals(4, couponList.size());
    }

    @Test
    void findCouponByIdTest() {
        Optional<CouponResponseDto> dto = couponRepository.findCouponById(1L);

        assertNotNull(dto);
        assertEquals(dto.get().getCouponId(), 1L);
        assertEquals(dto.get().getCouponName(), "Coupon 1");
        assertEquals(dto.get().getCouponStatus(), statuses[0]);
        assertEquals(dto.get().getCouponType(), types[0]);
        assertEquals(dto.get().getCouponTarget(), targets[0]);
    }

    @Test
    void findCouponsByContainingNameTest() {
        pageable = PageRequest.of(0, 10);
        Page<CouponResponseDto> dtoPage = couponRepository.findCouponsByContainingName("Coupon 1", pageable);
        List<CouponResponseDto> couponList = dtoPage.getContent();

        assertEquals(3, couponList.size());
    }

    @AfterEach
    public void teardown() {
        this.entityManager.createNativeQuery("ALTER TABLE coupon ALTER COLUMN coupon_id RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE category ALTER COLUMN category_id RESTART WITH 1").executeUpdate();
    }
}