package com.nhnacademy.shop.coupon.service.impl;

import com.nhnacademy.shop.coupon.dto.*;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon.exception.IllegalFormCouponRequestException;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.repository.*;
import com.nhnacademy.shop.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Coupon Service 구현체
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final AmountCouponRepository amountCouponRepository;
    private final BookCouponRepository bookCouponRepository;
    private final CategoryCouponRepository categoryCouponRepository;
    private final PercentageCouponRepository percentageCouponRepository;
    private final CouponRepository couponRepository;


    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::convertCouponToCouponDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getBookCoupons(String bookIsbn) {
        return bookCouponRepository.findAll().stream()
                .filter(coupon -> Objects.equals(coupon.getBookIsbn(), bookIsbn))
                .map(coupon -> convertCouponToCouponDto(coupon.getCoupon()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getCategoryCoupons(Long categoryId) {
        return categoryCouponRepository.findAll().stream()
                .filter(coupon -> Objects.equals(coupon.getCategoryId(), categoryId))
                .map(coupon -> convertCouponToCouponDto(coupon.getCoupon()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponDto getCouponById(Long couponId) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if(coupon.isEmpty()) {
            throw new NotFoundCouponException(couponId);
        }

        return convertCouponToCouponDto(coupon.get());
    }

    @Override
    @Transactional
    public List<CouponDto> getCouponsByContainingName(String couponName) {
        return couponRepository.findCouponsByContainingCouponName(couponName)
                .stream()
                .map(this::convertCouponToCouponDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponDto saveCoupon(CouponDto couponDto) {
        Coupon.CouponTarget target = couponDto.getCouponTarget();
        Coupon.CouponType type = couponDto.getCouponType();

        Coupon coupon = Coupon.builder()
                .couponName(couponDto.getCouponName())
                .deadline(couponDto.getDeadline())
                .couponStatus(couponDto.getCouponStatus())
                .couponType(couponDto.getCouponType())
                .couponTarget(couponDto.getCouponTarget())
                .build();

        couponRepository.save(coupon);

        if(target == Coupon.CouponTarget.BOOK){
            if(couponDto.getBookIsbn() == null) {
                throw new IllegalFormCouponRequestException();
            }

            BookCoupon bookCoupon = BookCoupon.builder()
                    .coupon(coupon)
                    .bookIsbn(couponDto.getBookIsbn())
                    .build();

            bookCouponRepository.save(bookCoupon);
        }
        else if(target == Coupon.CouponTarget.CATEGORY){
            if(couponDto.getCategoryId() == null) {
                throw new IllegalFormCouponRequestException();
            }

            CategoryCoupon categoryCoupon = CategoryCoupon.builder()
                    .coupon(coupon)
                    .categoryId(couponDto.getCategoryId())
                    .build();

            categoryCouponRepository.save(categoryCoupon);
        }

        if(type == Coupon.CouponType.AMOUNT) {
            if(couponDto.getDiscountPrice() == null) {
                throw new IllegalFormCouponRequestException();
            }

            AmountCoupon amountCoupon = AmountCoupon.builder()
                    .coupon(coupon)
                    .discountPrice(couponDto.getDiscountPrice())
                    .build();

            amountCouponRepository.save(amountCoupon);
        }
        else if(type == Coupon.CouponType.PERCENTAGE) {
            if(couponDto.getDiscountRate() == null || couponDto.getMaxDiscountPrice() == null) {
                throw new IllegalFormCouponRequestException();
            }

            PercentageCoupon percentageCoupon = PercentageCoupon.builder()
                    .coupon(coupon)
                    .discountRate(couponDto.getDiscountRate())
                    .maxDiscountPrice(couponDto.getMaxDiscountPrice())
                    .build();

            percentageCouponRepository.save(percentageCoupon);
        }

        return couponDto;
    }

    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);

        // 쿠폰 테이블에 존재하지 않는 경우, 에러 발생
        if(optionalCoupon.isEmpty()) {
            throw new NotFoundCouponException(couponId);
        }

        // 타입 별 하위 카테고리 확인 후, 삭제
        if(amountCouponRepository.existsById(couponId)) {
            amountCouponRepository.deleteById(couponId);
        }
        else if(percentageCouponRepository.existsById(couponId)) {
            percentageCouponRepository.deleteById(couponId);
        }

        // 타겟 별 하위 카테고리 확인 후, 삭제
        if(bookCouponRepository.existsById(couponId)) {
            bookCouponRepository.deleteById(couponId);
        }
        else if(categoryCouponRepository.existsById(couponId)) {
            categoryCouponRepository.deleteById(couponId);
        }

        couponRepository.deleteById(couponId);
    }

    /**
     * Convert coupon to couponDto method
     *
     * @param coupon
     * @return (ResponseCouponDto) convertedResponseCouponDto
     */
    public CouponDto convertCouponToCouponDto(Coupon coupon) {
        CouponDto couponDto = new CouponDto();

        couponDto.setCouponId(coupon.getCouponId());
        couponDto.setCouponStatus(coupon.getCouponStatus());
        couponDto.setCouponName(coupon.getCouponName());
        couponDto.setCouponTarget(coupon.getCouponTarget());
        couponDto.setCouponType(coupon.getCouponType());
        couponDto.setDeadline(coupon.getDeadline());

        Coupon.CouponTarget target = coupon.getCouponTarget();
        Coupon.CouponType type = coupon.getCouponType();

        if(target == Coupon.CouponTarget.BOOK){
            Optional<BookCoupon> optionalBookCoupon = bookCouponRepository.findById(coupon.getCouponId());

            if(optionalBookCoupon.isEmpty()) {
                throw new NotFoundCouponException(coupon.getCouponId());
            }

            couponDto.setBookIsbn(optionalBookCoupon.get().getBookIsbn());
        }
        else if(target == Coupon.CouponTarget.CATEGORY){
            Optional<CategoryCoupon> optionalCategoryCoupon = categoryCouponRepository.findById(coupon.getCouponId());

            if(optionalCategoryCoupon.isEmpty()) {
                throw new NotFoundCouponException(coupon.getCouponId());
            }

            couponDto.setCategoryId(optionalCategoryCoupon.get().getCategoryId());
        }

        if(type == Coupon.CouponType.AMOUNT) {
            Optional<AmountCoupon> optionalAmountCoupon = amountCouponRepository.findById(coupon.getCouponId());

            if(optionalAmountCoupon.isEmpty()) {
                throw new NotFoundCouponException(coupon.getCouponId());
            }

            couponDto.setDiscountPrice(optionalAmountCoupon.get().getDiscountPrice());
        }
        else if(type == Coupon.CouponType.PERCENTAGE) {
            Optional<PercentageCoupon> optionalPercentageCoupon = percentageCouponRepository.findById(coupon.getCouponId());

            if(optionalPercentageCoupon.isEmpty()) {
                throw new NotFoundCouponException(coupon.getCouponId());
            }

            couponDto.setMaxDiscountPrice(optionalPercentageCoupon.get().getMaxDiscountPrice());
            couponDto.setDiscountRate(optionalPercentageCoupon.get().getDiscountRate());
        }

        return couponDto;
    }
}
