package com.nhnacademy.shop.coupon_member.service;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.repository.CouponRepository;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.exception.MemberDoesNotHaveCouponException;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import com.nhnacademy.shop.coupon_member.service.impl.CouponMemberServiceImpl;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * CouponMember Service Test
 *
 * @Author : 박병휘
 * @Date : 2024/05/11
 */
public class CouponMemberServiceTest {
    @Mock
    private CouponMemberRepository couponMemberRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CouponMemberServiceImpl couponMemberService;

    private Member member;
    private Coupon coupon;
    private CouponMember couponMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        member = Member.builder()
                .memberId("123")
                .customerNo(1L)
                .customer(customer)
                .lastLoginAt(LocalDateTime.now())
                .grade(grade)
                .role("ROLE_ADMIN")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        coupon = Coupon.builder()
                .couponId(1L)
                .couponName("coupon")
                .deadline(LocalDate.now().plusDays(2))
                .issueLimit(2)
                .expirationPeriod(3)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.NORMAL)
                .build();

        couponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(2))
                .usedAt(null)
                .status(CouponMember.Status.ACTIVE)
                .build();
    }

    @Test
    @DisplayName(value="존재하는 couponMemberId로 조회")
    void getCouponIdByCouponMemberId() {
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.of(couponMember));

        assertEquals(couponMemberService.getCouponIdByCouponMemberId(1L), 1L);
    }

    @Test
    @DisplayName(value="존재하지 않는 couponMemberId로 조회")
    void getCouponIdByDoesNotExistCouponMemberId() {
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundCouponException.class, () -> couponMemberService.getCouponIdByCouponMemberId(1L));
    }

    @Test
    @DisplayName(value="couponMember 객체 생성")
    void createCouponMember() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(couponMemberRepository.save(any())).thenReturn(couponMember);
        when(couponRepository.findCouponById(1L)).thenReturn(Optional.of(
                CouponResponseDto.builder()
                        .couponId(1L)
                        .couponName(coupon.getCouponName())
                        .deadline(coupon.getDeadline())
                        .issueLimit(coupon.getIssueLimit())
                        .couponStatus(coupon.getCouponStatus())
                        .couponType(coupon.getCouponType())
                        .couponTarget(coupon.getCouponTarget())
                        .discountPrice(2000L)
                        .build()
        ));

        assertEquals(couponMemberService.createCouponMember(1L, 1L).getCouponId(), 1L);
        assertEquals(couponMemberService.createCouponMember(1L, 1L).getCustomerNo(), 1L);
    }

    @Test
    @DisplayName(value="없는 coupon에 대한 couponMember 객체 생성")
    void createCouponMemberWithNotFoundCoupon() {
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundCouponException.class, () -> couponMemberService.createCouponMember(1L, 1L));
    }

    @Test
    @DisplayName(value="없는 member에 대한 couponMember 객체 생성")
    void createCouponMemberWithNotFoundMember() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> couponMemberService.createCouponMember(1L, 1L));

    }

    @Test
    @DisplayName(value="member에 대한 couponMemberResponseDto List 추출")
    void getCouponMemberByMember() {
        Pageable pageable = PageRequest.of(1, 10);
        List<CouponMember> couponMemberList = List.of(couponMember, couponMember);
        Page<CouponMember> couponMemberPage = new PageImpl(couponMemberList, pageable, 2);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(couponRepository.findCouponById(1L)).thenReturn(Optional.of(
                CouponResponseDto.builder()
                        .couponId(1L)
                        .couponName(coupon.getCouponName())
                        .deadline(coupon.getDeadline())
                        .issueLimit(coupon.getIssueLimit())
                        .couponStatus(coupon.getCouponStatus())
                        .couponType(coupon.getCouponType())
                        .couponTarget(coupon.getCouponTarget())
                        .discountPrice(2000L)
                        .build()
        ));
        when(couponMemberRepository.findCouponMembersByMember_CustomerNo(1L, pageable))
                .thenReturn(couponMemberPage);

        assertEquals(couponMemberService.getCouponMemberByMember(1L, pageable).size(), 2);
    }

    @Test
    @DisplayName(value="없는 member에 대한 couponMemberResponseDto List 추출")
    void getCouponMemberByNotFoundMember() {
        Pageable pageable = PageRequest.of(1, 10);
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> couponMemberService.getCouponMemberByMember(1L, pageable));
    }

    @Test
    @DisplayName(value="member에 대한 couponMember Status 변경(ACTIVE -> USED)")
    void modifyCouponMemberStatus() {
        CouponMember changedCouponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(2))
                .usedAt(null)
                .status(CouponMember.Status.USED)
                .build();
        CouponResponseDto couponResponseDto = CouponResponseDto.builder()
                .couponId(1L)
                .couponName(coupon.getCouponName())
                .deadline(coupon.getDeadline())
                .issueLimit(coupon.getIssueLimit())
                .couponStatus(coupon.getCouponStatus())
                .couponType(coupon.getCouponType())
                .couponTarget(coupon.getCouponTarget())
                .discountPrice(2000L)
                .build();

        when(couponRepository.findCouponById(1L)).thenReturn(Optional.of(couponResponseDto));
        when(couponMemberRepository.save(any())).thenReturn(changedCouponMember);
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.of(couponMember));

        assertEquals(couponMemberService.modifyCouponMemberStatus(1L, CouponMember.Status.USED).getCouponStatus(), CouponMember.Status.USED);
    }

    @Test
    @DisplayName(value="member에 대한 couponMember Status 변경(ACTIVE -> DESTROYED)")
    void modifyCouponMemberStatusActiveToDestroyed() {
        CouponMember changedCouponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(2))
                .usedAt(null)
                .status(CouponMember.Status.DESTROYED)
                .build();
        CouponResponseDto couponResponseDto = CouponResponseDto.builder()
                .couponId(1L)
                .couponName(coupon.getCouponName())
                .deadline(coupon.getDeadline())
                .issueLimit(coupon.getIssueLimit())
                .couponStatus(coupon.getCouponStatus())
                .couponType(coupon.getCouponType())
                .couponTarget(coupon.getCouponTarget())
                .discountPrice(2000L)
                .build();

        when(couponRepository.findCouponById(1L)).thenReturn(Optional.of(couponResponseDto));
        when(couponMemberRepository.save(any())).thenReturn(changedCouponMember);
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.of(couponMember));

        assertEquals(couponMemberService.modifyCouponMemberStatus(1L, CouponMember.Status.DESTROYED).getCouponStatus(), CouponMember.Status.DESTROYED);
    }

    @Test
    @DisplayName(value="member에 대한 couponMember Status 변경(ACTIVE -> ACTIVE)")
    void modifyCouponMemberStatusDestroyedToUsed() {
        CouponMember changedCouponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(2))
                .usedAt(null)
                .status(CouponMember.Status.ACTIVE)
                .build();
        CouponResponseDto couponResponseDto = CouponResponseDto.builder()
                .couponId(1L)
                .couponName(coupon.getCouponName())
                .deadline(coupon.getDeadline())
                .issueLimit(coupon.getIssueLimit())
                .couponStatus(coupon.getCouponStatus())
                .couponType(coupon.getCouponType())
                .couponTarget(coupon.getCouponTarget())
                .discountPrice(2000L)
                .build();

        when(couponRepository.findCouponById(1L)).thenReturn(Optional.of(couponResponseDto));
        when(couponMemberRepository.save(any())).thenReturn(changedCouponMember);
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.of(couponMember));

        assertEquals(couponMemberService.modifyCouponMemberStatus(1L, CouponMember.Status.ACTIVE).getCouponStatus(), CouponMember.Status.ACTIVE);
    }

    @Test
    @DisplayName(value="member에 대한 couponMember Status 변경(USED -> ACTIVE)")
    void modifyCouponMemberStatusActiveToUsed() {
        CouponMember beforeCouponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now().plusDays(2))
                .usedAt(null)
                .status(CouponMember.Status.USED)
                .build();
        CouponResponseDto couponResponseDto = CouponResponseDto.builder()
                .couponId(1L)
                .couponName(coupon.getCouponName())
                .deadline(coupon.getDeadline())
                .issueLimit(coupon.getIssueLimit())
                .couponStatus(coupon.getCouponStatus())
                .couponType(coupon.getCouponType())
                .couponTarget(coupon.getCouponTarget())
                .discountPrice(2000L)
                .build();

        when(couponRepository.findCouponById(1L)).thenReturn(Optional.of(couponResponseDto));
        when(couponMemberRepository.save(any())).thenReturn(couponMember);
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.of(beforeCouponMember));

        assertEquals(couponMemberService.modifyCouponMemberStatus(1L, CouponMember.Status.ACTIVE).getCouponStatus(), CouponMember.Status.ACTIVE);
    }

    @Test
    @DisplayName(value="없는 couponMember에 대한 couponMember Status 변경")
    void modifyCouponMemberStatusWithNotFoundMember() {
        when(couponMemberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberDoesNotHaveCouponException.class, () -> couponMemberService.modifyCouponMemberStatus(1L, CouponMember.Status.ACTIVE));
    }
}
