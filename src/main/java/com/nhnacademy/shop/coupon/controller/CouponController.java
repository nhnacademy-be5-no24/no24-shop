package com.nhnacademy.shop.coupon.controller;

import com.nhnacademy.shop.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDtoList;
import com.nhnacademy.shop.coupon.exception.IllegalFormCouponRequestException;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Coupon Controller
 *
 * @author : 박병휘, 강병구
 * @date : 2024/03/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/coupon")
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<CouponResponseDtoList> getCoupons(@RequestParam Integer pageSize,
                                                            @RequestParam Integer offset) {
        try {
            Page<CouponResponseDto> couponDtoList = couponService.getAllCoupons(pageSize, offset);
            CouponResponseDtoList couponResponseDtoList = CouponResponseDtoList.builder()
                    .couponResponseDtoList(couponDtoList.getContent())
                    .build();

            return ResponseEntity.ok(couponResponseDtoList);
        } catch(NotFoundCouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/available/{customerNo}")
    public ResponseEntity<CouponResponseDtoList> getCoupons(@PathVariable Long customerNo,
                                                            @RequestParam Integer pageSize,
                                                            @RequestParam Integer offset) {
        try {
            Page<CouponResponseDto> couponDtoList = couponService.getAllAvailableCoupons(customerNo, pageSize, offset);
            CouponResponseDtoList couponResponseDtoList = CouponResponseDtoList.builder()
                    .couponResponseDtoList(couponDtoList.getContent())
                    .build();

            return ResponseEntity.ok(couponResponseDtoList);
        } catch(NotFoundCouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> getCoupon(@PathVariable("couponId") Long couponId) {
        try {
            CouponResponseDto couponDto = couponService.getCouponById(couponId);
            return ResponseEntity.ok(couponDto);
        } catch(NotFoundCouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search/{couponName}")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByContainingName(@PathVariable("couponName") String couponName,
                                                                              @RequestParam Integer pageSize,
                                                                              @RequestParam Integer offset) {
        try {
            Page<CouponResponseDto> couponDtoList = couponService.getCouponsByContainingName(couponName, pageSize, offset);
            return ResponseEntity.ok(couponDtoList);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByCategoryId(@PathVariable("categoryId") Long categoryId,
                                                                          @RequestParam Integer pageSize,
                                                                          @RequestParam Integer offset) {
        try {
            Page<CouponResponseDto> couponDtoList = couponService.getCategoryCoupons(categoryId, pageSize, offset);
            return ResponseEntity.ok(couponDtoList);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/book/{bookIsbn}")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByBookIsbn(@PathVariable("bookIsbn") String bookIsbn,
                                                                        @RequestParam Integer pageSize,
                                                                        @RequestParam Integer offset) {
        try {
            Page<CouponResponseDto> couponDtoList = couponService.getBookCoupons(bookIsbn, pageSize, offset);
            return ResponseEntity.ok(couponDtoList);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CouponResponseDto> createCoupon(@RequestBody CouponRequestDto couponDtoList) {
        try {
            CouponResponseDto dto = couponService.saveCoupon(couponDtoList);
            return ResponseEntity.ok(dto);
        } catch(IllegalFormCouponRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create/coupons")
    public ResponseEntity<List<CouponResponseDto>> createCoupons(@RequestBody List<CouponRequestDto> couponDtoList) {
        try {
            List<CouponResponseDto> dtoList = new ArrayList<>();
            for(CouponRequestDto requestCouponDto : couponDtoList) {
                dtoList.add(couponService.saveCoupon(requestCouponDto));

            }
            return ResponseEntity.ok(dtoList);
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
