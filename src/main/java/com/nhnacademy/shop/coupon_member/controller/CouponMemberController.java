package com.nhnacademy.shop.coupon_member.controller;

import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon_member.dto.request.CouponMemberRequestDto;
import com.nhnacademy.shop.coupon_member.dto.request.CouponMemberStatusRequestDto;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDtoList;
import com.nhnacademy.shop.coupon_member.service.CouponMemberService;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Coupon과 Member의 연관관계를 수정하는 Controller
 *
 * @Author : 박병휘
 * @Date : 2024/04/26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/coupon/member")
public class CouponMemberController {
    private final CouponMemberService couponMemberService;

    @GetMapping("/couponMemberId/{couponMemberId}")
    public ResponseEntity<Long> getCouponIdByCouponMemberId(@PathVariable("couponMemberId") Long couponMemberId) {
        try {
            Long couponId = couponMemberService.getCouponIdByCouponMemberId(couponMemberId);

            return ResponseEntity.ok(couponId);
        } catch (NotFoundCouponException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{customerNo}")
    public ResponseEntity<CouponMemberResponseDtoList> getCouponMemberByMember(@PathVariable Long customerNo,
                                                                                @RequestParam Integer pageSize,
                                                                                @RequestParam Integer offset) {
        Pageable pageable = PageRequest.of(pageSize, offset);
        try {
            CouponMemberResponseDtoList response = couponMemberService.getCouponMemberByMember(customerNo, pageable);

            return ResponseEntity.ok(response);
        } catch (MemberNotFoundException memberNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (NotFoundCouponException notFoundCouponException) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{customerNo}/{couponId}")
    public ResponseEntity<CouponMemberResponseDto> createCouponMember(@PathVariable Long customerNo,
                                                                       @PathVariable Long couponId) {
        try {
            CouponMemberResponseDto response = couponMemberService.createCouponMember(couponId, customerNo);

            return ResponseEntity.ok(response);
        } catch (MemberNotFoundException memberNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (NotFoundCouponException notFoundCouponException) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{couponMemberId}")
    public ResponseEntity<CouponMemberResponseDto> modifyCouponMember(@PathVariable Long couponMemberId,
                                                                       @RequestBody CouponMemberStatusRequestDto requestDto) {
        try {
            CouponMemberResponseDto response = couponMemberService.modifyCouponMemberStatus(couponMemberId, requestDto.getStatus());

            return ResponseEntity.ok(response);
        } catch (MemberNotFoundException memberNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (NotFoundCouponException notFoundCouponException) {
            return ResponseEntity.notFound().build();
        }
    }
}
