package com.nhnacademy.shop.coupon.controller;

import com.nhnacademy.shop.coupon.dto.*;
import com.nhnacademy.shop.coupon.exception.IllegalFormCouponRequestException;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Coupon Controller
 *
 * @Author : 박병휘
 * @Date : 2024/03/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/coupon")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/")
    public ResponseEntity<List<CouponDto>> getCoupons() {
        try {
            List<CouponDto> couponDtoList = couponService.getAllCoupons();
            return ResponseEntity.ok(couponDtoList);
        } catch(NotFoundCouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable("couponId") Long couponId) {
        try {
            CouponDto couponDto = couponService.getCouponById(couponId);
            return ResponseEntity.ok(couponDto);
        } catch(NotFoundCouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search/{couponName}")
    public ResponseEntity<List<CouponDto>> getCouponsByContainingName(@PathVariable("couponName") String couponName) {
        try {
            List<CouponDto> couponDtoList = couponService.getCouponsByContainingName(couponName);
            return ResponseEntity.ok(couponDtoList);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CouponDto>> getCouponsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        try {
            List<CouponDto> couponDtoList = couponService.getCategoryCoupons(categoryId);
            return ResponseEntity.ok(couponDtoList);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/book/{bookIsbn}")
    public ResponseEntity<List<CouponDto>> getCouponsByBookIsbn(@PathVariable("bookIsbn") String bookIsbn) {
        try {
            List<CouponDto> couponDtoList = couponService.getBookCoupons(bookIsbn);
            return ResponseEntity.ok(couponDtoList);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CouponDto> createCoupon(@RequestBody CouponDto couponDtoList) {
        try {
            couponService.saveCoupon(couponDtoList);
            return ResponseEntity.ok(couponDtoList);
        } catch(IllegalFormCouponRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create/coupons")
    public ResponseEntity<List<CouponDto>> createCoupons(@RequestBody List<CouponDto> couponDtoList) {
        try {
            for(CouponDto requestCouponDto : couponDtoList) {
                couponService.saveCoupon(requestCouponDto);
            }
            return ResponseEntity.ok(couponDtoList);
        } catch(IllegalFormCouponRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable("couponId") Long couponId) {
        try {
            couponService.deleteCoupon(couponId);
            return ResponseEntity.ok().build();
        } catch(NotFoundCouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
