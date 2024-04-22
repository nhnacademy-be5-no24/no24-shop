package com.nhnacademy.shop.coupon_member.repository.impl;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.domain.QCouponMember;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepositoryCustom;
import com.nhnacademy.shop.member.domain.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/21
 */
public class CouponMemberRepositoryImpl extends QuerydslRepositorySupport implements CouponMemberRepositoryCustom {
    public CouponMemberRepositoryImpl() {
        super(CouponMember.class);
    }

    QCoupon coupon = QCoupon.coupon;
    QMember member = QMember.member;
    QAmountCoupon amountCoupon = QAmountCoupon.amountCoupon;
    QBookCoupon bookCoupon = QBookCoupon.bookCoupon;
    QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;
    QPercentageCoupon percentageCoupon = QPercentageCoupon.percentageCoupon;
    QCouponMember couponMember = QCouponMember.couponMember;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findByMemberCustomerNo(Long customerNo, Pageable pageable) {
        JPQLQuery<Long> count = from(couponMember)
                .select(couponMember.count());

        List<CouponResponseDto> content = from(couponMember)
                .leftJoin(coupon).on(couponMember.coupon.eq(coupon))
                .leftJoin(member).on(couponMember.member.eq(member))
                .where(member.customerNo.eq(customerNo))
                .leftJoin(amountCoupon).on(coupon.couponId.eq(amountCoupon.couponId))
                .leftJoin(percentageCoupon).on(coupon.couponId.eq(percentageCoupon.couponId))
                .leftJoin(bookCoupon).on(coupon.couponId.eq(bookCoupon.couponId))
                .leftJoin(categoryCoupon).on(coupon.couponId.eq(categoryCoupon.couponId))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.issueLimit,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget,
                        categoryCoupon.category.categoryId,
                        bookCoupon.book.bookIsbn,
                        amountCoupon.discountPrice,
                        percentageCoupon.discountRate,
                        percentageCoupon.maxDiscountPrice))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findUnusedCouponByMemberCustomerNo(Long customerNo, Pageable pageable) {
        JPQLQuery<Long> count = from(couponMember)
                .select(couponMember.count());

        List<CouponResponseDto> content = from(couponMember)
                .leftJoin(coupon).on(couponMember.coupon.eq(coupon))
                .leftJoin(member).on(couponMember.member.eq(member))
                .where(member.customerNo.eq(customerNo))
                .where(couponMember.used.eq(Boolean.FALSE))
                .leftJoin(amountCoupon).on(coupon.couponId.eq(amountCoupon.couponId))
                .leftJoin(percentageCoupon).on(coupon.couponId.eq(percentageCoupon.couponId))
                .leftJoin(bookCoupon).on(coupon.couponId.eq(bookCoupon.couponId))
                .leftJoin(categoryCoupon).on(coupon.couponId.eq(categoryCoupon.couponId))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.issueLimit,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget,
                        categoryCoupon.category.categoryId,
                        bookCoupon.book.bookIsbn,
                        amountCoupon.discountPrice,
                        percentageCoupon.discountRate,
                        percentageCoupon.maxDiscountPrice))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findUnusedBookCouponByMemberCustomerNo(Long customerNo, String bookIsbn, Pageable pageable) {
        JPQLQuery<Long> count = from(couponMember)
                .select(couponMember.count());

        List<CouponResponseDto> content = from(couponMember)
                .leftJoin(coupon).on(couponMember.coupon.eq(coupon))
                .leftJoin(member).on(couponMember.member.eq(member))
                .where(member.customerNo.eq(customerNo))
                .where(couponMember.used.eq(Boolean.FALSE))
                .leftJoin(amountCoupon).on(coupon.couponId.eq(amountCoupon.couponId))
                .leftJoin(percentageCoupon).on(coupon.couponId.eq(percentageCoupon.couponId))
                .leftJoin(bookCoupon).on(coupon.couponId.eq(bookCoupon.couponId))
                .leftJoin(categoryCoupon).on(coupon.couponId.eq(categoryCoupon.couponId))
                .where(bookCoupon.book.bookIsbn.eq(bookIsbn))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.issueLimit,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget,
                        categoryCoupon.category.categoryId,
                        bookCoupon.book.bookIsbn,
                        amountCoupon.discountPrice,
                        percentageCoupon.discountRate,
                        percentageCoupon.maxDiscountPrice))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findUnusedCategoryCouponByMemberCustomerNoAndCategoryId(Long customerNo, Long categoryId, Pageable pageable) {
        JPQLQuery<Long> count = from(couponMember)
                .select(couponMember.count());

        List<CouponResponseDto> content = from(couponMember)
                .leftJoin(coupon).on(couponMember.coupon.eq(coupon))
                .leftJoin(member).on(couponMember.member.eq(member))
                .where(member.customerNo.eq(customerNo))
                .where(couponMember.used.eq(Boolean.FALSE))
                .leftJoin(amountCoupon).on(coupon.couponId.eq(amountCoupon.couponId))
                .leftJoin(percentageCoupon).on(coupon.couponId.eq(percentageCoupon.couponId))
                .leftJoin(bookCoupon).on(coupon.couponId.eq(bookCoupon.couponId))
                .leftJoin(categoryCoupon).on(coupon.couponId.eq(categoryCoupon.couponId))
                .where(categoryCoupon.category.categoryId.eq(categoryId))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.issueLimit,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget,
                        categoryCoupon.category.categoryId,
                        bookCoupon.book.bookIsbn,
                        amountCoupon.discountPrice,
                        percentageCoupon.discountRate,
                        percentageCoupon.maxDiscountPrice))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
