package com.nhnacademy.shop.coupon.service.impl;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.exception.CategoryNotFoundException;
import com.nhnacademy.shop.category.repository.CategoryRepository;
import com.nhnacademy.shop.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.*;
import com.nhnacademy.shop.coupon.exception.IllegalFormCouponRequestException;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.repository.*;
import com.nhnacademy.shop.coupon.service.CouponService;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Coupon Service 구현체
 *
 * @author : 박병휘, 강병구
 * @date : 2024/03/29
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponMemberRepository couponMemberRepository;
    private final AmountCouponRepository amountCouponRepository;
    private final BookCouponRepository bookCouponRepository;
    private final CategoryCouponRepository categoryCouponRepository;
    private final PercentageCouponRepository percentageCouponRepository;
    private final CouponRepository couponRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getAllCoupons(Integer pageSize, Integer offset) {
        return couponRepository.findAllCoupons(PageRequest.of(pageSize, offset));
    }

    @Override
    public Page<CouponResponseDto> getAllAvailableCoupons(Long customerNo, Integer pageSize, Integer offset) {
        List<Long> memberCouponIdList = couponMemberRepository.findCouponMembersByMember_CustomerNo(customerNo).stream()
                .map(couponMember -> couponMember.getCoupon().getCouponId())
                .collect(Collectors.toList());

        Map<Long, Integer> couponReceiveMap = new HashMap<>();

        for(Long couponId: memberCouponIdList) {
            if(couponReceiveMap.containsKey(couponId)) {
                couponReceiveMap.put(couponId, couponReceiveMap.get(couponId) + 1);
            }
            else {
                couponReceiveMap.put(couponId, 1);
            }
        }

        List<Coupon> couponList1 = couponRepository.findAll();

        List<CouponResponseDto> couponList = couponRepository.findAll().stream()
                .filter(coupon -> !couponReceiveMap.containsKey(coupon.getCouponId()) || couponReceiveMap.get(coupon.getCouponId()) < coupon.getIssueLimit())
                .filter(coupon -> coupon.getDeadline().atStartOfDay().isAfter(LocalDateTime.now()))
                .map(coupon -> {
                    CouponResponseDto couponResponseDto = CouponResponseDto.builder()
                            .couponId(coupon.getCouponId())
                            .couponName(coupon.getCouponName())
                            .deadline(coupon.getDeadline())
                            .issueLimit(coupon.getIssueLimit())
                            .couponStatus(coupon.getCouponStatus())
                            .couponType(coupon.getCouponType())
                            .couponTarget(coupon.getCouponTarget())
                            .build();

                    // for Type
                    if(coupon.getCouponType() == Coupon.CouponType.AMOUNT) {
                        AmountCoupon amountCoupon = amountCouponRepository.findById(coupon.getCouponId()).get();
                        couponResponseDto.setDiscountPrice(amountCoupon.getDiscountPrice());
                    }
                    else {
                        PercentageCoupon percentageCoupon = percentageCouponRepository.findById(coupon.getCouponId()).get();
                        couponResponseDto.setDiscountRate(percentageCoupon.getDiscountRate());
                        couponResponseDto.setMaxDiscountPrice(percentageCoupon.getMaxDiscountPrice());
                    }

                    // for target
                    if(coupon.getCouponTarget() == Coupon.CouponTarget.BOOK) {
                        BookCoupon bookCoupon = bookCouponRepository.findById(coupon.getCouponId()).get();
                        couponResponseDto.setBookIsbn(bookCoupon.getBook().getBookIsbn());
                    }
                    else if(coupon.getCouponTarget() == Coupon.CouponTarget.CATEGORY) {
                        CategoryCoupon categoryCoupon = categoryCouponRepository.findById(coupon.getCouponId()).get();
                        couponResponseDto.setCategoryId(categoryCoupon.getCategory().getCategoryId());
                    }

                    return couponResponseDto;
                }).collect(Collectors.toList());

        int start = (pageSize - 1) * offset;
        int end = Math.min(start + offset, couponList.size());

        return new PageImpl<>(couponList.subList(start, end), PageRequest.of(pageSize, offset), couponList.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getBookCoupons(String bookIsbn, Integer pageSize, Integer offset) {
        return couponRepository.findBookCoupons(bookIsbn, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getCategoryCoupons(Long categoryId, Integer pageSize, Integer offset) {
        return couponRepository.findCategoryCoupons(categoryId, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CouponResponseDto getCouponById(Long couponId) {
        return couponRepository.findCouponById(couponId).orElseThrow(() -> new NotFoundCouponException(couponId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getCouponsByContainingName(String couponName, Integer pageSize, Integer offset) {
        return couponRepository.findCouponsByContainingName(couponName, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CouponResponseDto saveCoupon(CouponRequestDto couponDto) {
        Coupon.CouponTarget target = couponDto.getCouponTarget();
        Coupon.CouponType type = couponDto.getCouponType();

        Coupon coupon = Coupon.builder()
                .couponName(couponDto.getCouponName())
                .deadline(couponDto.getDeadline())
                .issueLimit(couponDto.getIssueLimit())
                .couponStatus(couponDto.getCouponStatus())
                .couponType(couponDto.getCouponType())
                .couponTarget(couponDto.getCouponTarget())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        CouponResponseDto dto = CouponResponseDto.builder()
                .couponName(savedCoupon.getCouponName())
                .deadline(savedCoupon.getDeadline())
                .issueLimit(savedCoupon.getIssueLimit())
                .couponStatus(savedCoupon.getCouponStatus())
                .couponType(savedCoupon.getCouponType())
                .couponTarget(savedCoupon.getCouponTarget())
                .build();

        if(target == Coupon.CouponTarget.BOOK){
            if(couponDto.getBookIsbn() == null) {
                throw new IllegalFormCouponRequestException();
            }

            Optional<Book> book = bookRepository.findByBookIsbn(couponDto.getBookIsbn());

            if(book.isEmpty()) {
                throw new BookNotFoundException();
            }

            BookCoupon bookCoupon = BookCoupon.builder()
                    .coupon(coupon)
                    .book(book.get())
                    .build();

            bookCouponRepository.save(bookCoupon);
        }
        else if(target == Coupon.CouponTarget.CATEGORY){
            if(couponDto.getCategoryId() == null) {
                throw new IllegalFormCouponRequestException();
            }

            Optional<Category> category = categoryRepository.findById(couponDto.getCategoryId());

            if(category.isEmpty()) {
                throw new CategoryNotFoundException();
            }

            CategoryCoupon categoryCoupon = CategoryCoupon.builder()
                    .coupon(coupon)
                    .category(category.get())
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

        return dto;
    }

    /**
     * {@inheritDoc}
     */
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
}
