package com.nhnacademy.shop.coupon.repository.impl;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon.repository.CouponRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 쿠폰 레포지토리 구현체 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-11
 */
public class CouponRepositoryImpl extends QuerydslRepositorySupport implements CouponRepositoryCustom {
    public CouponRepositoryImpl() {
        super(Coupon.class);
    }

    QCoupon coupon = QCoupon.coupon;
    QAmountCoupon amountCoupon = QAmountCoupon.amountCoupon;
    QBookCoupon bookCoupon = QBookCoupon.bookCoupon;
    QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;
    QPercentageCoupon percentageCoupon = QPercentageCoupon.percentageCoupon;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findAllCoupons(Pageable pageable) {
        JPQLQuery<Long> count = from(coupon)
                .select(coupon.count());

        List<CouponResponseDto> content = from(coupon)
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findBookCoupons(String bookIsbn, Pageable pageable) {
        JPQLQuery<Long> count = from(coupon)
                .innerJoin(bookCoupon)
                .select(bookCoupon.count())
                .where(coupon.couponId.eq(bookCoupon.couponId)
                        .and(bookCoupon.bookIsbn.eq(bookIsbn)));

        List<CouponResponseDto> content = from(coupon)
                .innerJoin(bookCoupon)
                .leftJoin(amountCoupon).on(coupon.couponId.eq(amountCoupon.couponId))
                .leftJoin(percentageCoupon).on(coupon.couponId.eq(percentageCoupon.couponId))
                .where(coupon.couponId.eq(bookCoupon.couponId)
                        .and(bookCoupon.bookIsbn.eq(bookIsbn)))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget,
                        bookCoupon.bookIsbn,
                        amountCoupon.discountPrice.coalesce(0L).as("DiscountPrice"),
                        percentageCoupon.discountRate.coalesce(0L).as("DiscountRate"),
                        percentageCoupon.maxDiscountPrice.coalesce(0L).as("MaxDiscountPrice")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findCategoryCoupons(Long categoryId, Pageable pageable) {
        JPQLQuery<Long> count = from(coupon)
                .innerJoin(categoryCoupon)
                .select(categoryCoupon.count())
                .where(coupon.couponId.eq(categoryCoupon.couponId)
                        .and(categoryCoupon.categoryId.eq(categoryId)));

        List<CouponResponseDto> content = from(coupon)
                .innerJoin(categoryCoupon)
                .leftJoin(amountCoupon).on(coupon.couponId.eq(amountCoupon.couponId))
                .leftJoin(percentageCoupon).on(coupon.couponId.eq(percentageCoupon.couponId))
                .where(coupon.couponId.eq(categoryCoupon.couponId)
                        .and(categoryCoupon.categoryId.eq(categoryId)))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget,
                        categoryCoupon.categoryId,
                        amountCoupon.discountPrice.coalesce(0L).as("DiscountPrice"),
                        percentageCoupon.discountRate.coalesce(0L).as("DiscountRate"),
                        percentageCoupon.maxDiscountPrice.coalesce(0L).as("MaxDiscountPrice")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CouponResponseDto> findCouponById(Long couponId) {
        CouponResponseDto dto = from(coupon)
                .where(coupon.couponId.eq(couponId))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget))
                .fetchOne();

        return Optional.of(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CouponResponseDto> findCouponsByContainingName(String couponName, Pageable pageable) {
        JPQLQuery<Long> count = from(coupon)
                .select(coupon.count())
                .where(coupon.couponName.like("%" + couponName + "%"));

        List<CouponResponseDto> content = from(coupon)
                .where(coupon.couponName.like("%" + couponName + "%"))
                .select(Projections.fields(CouponResponseDto.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.deadline,
                        coupon.couponStatus,
                        coupon.couponType,
                        coupon.couponTarget))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
