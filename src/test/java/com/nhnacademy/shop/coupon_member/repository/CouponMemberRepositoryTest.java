package com.nhnacademy.shop.coupon_member.repository;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon.repository.*;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CouponMember Repository Test.
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class CouponMemberRepositoryTest {
    private Pageable pageable;
    private Coupon coupon;
    private AmountCoupon amountCoupon;
    private PercentageCoupon percentageCoupon;
    private CategoryCoupon categoryCoupon;
    private BookCoupon bookCoupon;
    private Book book;
    private Category category;
    private Member member;
    private Customer customer;
    private Grade grade;
    private List<Coupon> bookCouponList = new ArrayList<>();
    private List<Coupon> categoryCouponList = new ArrayList<>();

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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GradeRepository gradeRespository;
    @Autowired
    private CouponMemberRepository couponMemberRepository;

    @BeforeEach
    void setUp() {
        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .accumulateRate(5L)
                .build();

        grade = gradeRespository.save(grade);

        customer = Customer.builder()
                .customerNo(1L)
                .customerId("123")
                .customerPassword("1234")
                .customerName("홍길동")
                .customerPhoneNumber("01051745441")
                .customerEmail("jin@naver.com")
                .customerBirthday(LocalDate.of(2001, 2, 21))
                .customerRole("ROLE_MEMBER")
                .build();

        customer = customerRepository.save(customer);

        member = Member.builder()
                .memberId("123")
                .customer(customer)
                .lastLoginAt(LocalDateTime.now())
                .grade(grade)
                .role("ROLE_MEMBER")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        member = memberRepository.save(member);

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
                            .issueLimit(2)
                            .expirationPeriod(3)
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
                        bookCouponList.add(coupon);
                    }
                    else if(targets[k] == Coupon.CouponTarget.CATEGORY) {
                        categoryCoupon = CategoryCoupon.builder()
                                .coupon(coupon)
                                .category(category)
                                .build();
                        CategoryCoupon c = categoryCouponRepository.save(categoryCoupon);
                        categoryCouponList.add(coupon);
                    }
                }
            }
        }
    }

    @Test
    public void couponMemberSaveTest() {
        CouponMember couponMember = CouponMember.builder()
                .couponMemberId(null)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(coupon.getExpirationPeriod()))
                .usedAt(null)
                .build();

        entityManager.persist(couponMember);

        assertEquals(couponMember, couponMemberRepository.findById(1L).get());
    }

    @Test
    public void findByMemberCustomerNoTest() {
        CouponMember couponMember = CouponMember.builder()
                .couponMemberId(null)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(coupon.getExpirationPeriod()))
                .usedAt(null)
                .build();

        entityManager.persist(couponMember);

        CouponResponseDto couponResponseDto = couponMemberRepository.findByMemberCustomerNo(member.getCustomerNo(), pageable).getContent().get(0);

        assertEquals(couponResponseDto, couponRepository.findCouponById(11L).get());
    }

    @Test
    public void findUnusedCouponByMemberCustomerNoTest() {
        boolean[] booleans = {true, false};

        for(Boolean bool : booleans) {
            CouponMember couponMember = CouponMember.builder()
                    .couponMemberId(null)
                    .coupon(coupon)
                    .member(member)
                    .used(bool)
                    .createdAt(LocalDateTime.now())
                    .destroyedAt(LocalDateTime.now().plusDays(coupon.getExpirationPeriod()))
                    .usedAt(null)
                    .build();

            entityManager.persist(couponMember);
        }

        Page<CouponResponseDto> page = couponMemberRepository.findUnusedCouponByMemberCustomerNo(member.getCustomerNo(), pageable);

        assertEquals(page.getContent().size(), 1);
    }

    @Test
    public void findUnusedBookCouponByMemberCustomerNoTest() {
        boolean[] booleans = {true, false};

        for(Boolean bool : booleans) {
            for (Coupon coupon : bookCouponList) {
                CouponMember couponMember = CouponMember.builder()
                        .couponMemberId(null)
                        .coupon(coupon)
                        .member(member)
                        .used(bool)
                        .createdAt(LocalDateTime.now())
                        .destroyedAt(LocalDateTime.now().plusDays(coupon.getExpirationPeriod()))
                        .usedAt(null)
                        .build();

                entityManager.persist(couponMember);
            }
        }

        Page<CouponResponseDto> page = couponMemberRepository.findUnusedBookCouponByMemberCustomerNo(member.getCustomerNo(), "ABC123", pageable);

        assertEquals(page.getContent().size(), 4);
    }

    @Test
    public void findUnusedCategoryCouponByMemberCustomerNoTest() {
        boolean[] booleans = {true, false};

        for(Boolean bool : booleans) {
            for (Coupon coupon : categoryCouponList) {
                CouponMember couponMember = CouponMember.builder()
                        .couponMemberId(null)
                        .coupon(coupon)
                        .member(member)
                        .used(bool)
                        .createdAt(LocalDateTime.now())
                        .destroyedAt(LocalDateTime.now().plusDays(coupon.getExpirationPeriod()))
                        .usedAt(null)
                        .build();

                entityManager.persist(couponMember);
            }
        }

        Page<CouponResponseDto> page = couponMemberRepository.findUnusedCategoryCouponByMemberCustomerNoAndCategoryId(member.getCustomerNo(), 1L, pageable);

        assertEquals(page.getContent().size(), 4);
    }

    @AfterEach
    public void teardown() {
        this.entityManager.createNativeQuery("ALTER TABLE coupon ALTER COLUMN coupon_id RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE category ALTER COLUMN category_id RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE customer ALTER COLUMN customer_no RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE coupon_member ALTER COLUMN coupon_member_id RESTART WITH 1").executeUpdate();
    }
}
