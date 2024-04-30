package com.nhnacademy.shop.coupon_member.service.impl;

import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.repository.CouponRepository;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.coupon_member.exception.MemberDoesNotHaveCouponException;
import com.nhnacademy.shop.coupon_member.repository.CouponMemberRepository;
import com.nhnacademy.shop.coupon_member.service.CouponMemberService;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Coupon과 Member의 연관 테이블 저장/수정/삭제하는 서비스
 *
 * @Author : 박병휘
 * @Date : 2024/04/26
 */
@Service
@RequiredArgsConstructor
public class CouponMemberServiceImpl implements CouponMemberService {
    private final CouponMemberRepository couponMemberRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Long getCouponIdByCouponMemberId(Long couponMemberId) {
        Optional<CouponMember> couponMember = couponMemberRepository.findById(couponMemberId);

        if(couponMember.isEmpty()) {
            throw new NotFoundCouponException(couponMemberId);
        }

        return couponMember.get().getCoupon().getCouponId();
    }


    @Override
    @Transactional
    public CouponMemberResponseDto createCouponMember(Long couponId, Long customerNo) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);
        Optional<Member> optionalMember = memberRepository.findById(customerNo);

        if(optionalCoupon.isEmpty()) {
            throw new NotFoundCouponException(couponId);
        }

        if(optionalMember.isEmpty()) {
            throw new MemberNotFoundException();
        }

        CouponMember couponMember = CouponMember.builder()
                .couponMemberId(null)
                .coupon(optionalCoupon.get())
                .member(optionalMember.get())
                .used(false)
                .createdAt(LocalDateTime.now())
                .destroyedAt(null)
                .usedAt(null)
                .status(CouponMember.Status.ACTIVE)
                .build();

        couponMember = couponMemberRepository.save(couponMember);
        CouponResponseDto couponResponseDto = couponRepository.findCouponById(couponId).get();

        return CouponMemberResponseDto.buildDto(couponMember, couponResponseDto);
    }

    @Override
    @Transactional
    public List<CouponMemberResponseDto> getCouponMemberByMember(Long customerNo, Pageable pageable) throws MemberNotFoundException, NotFoundCouponException {
        Optional<Member> optionalMember = memberRepository.findById(customerNo);

        if(optionalMember.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Page<CouponMember> couponMembers = couponMemberRepository.findCouponMembersByMember_CustomerNo(customerNo, pageable);
        List<CouponMember> couponMemberDtoList = couponMembers.getContent();

        List<CouponMemberResponseDto> couponMemberResponseDtoList  = couponMemberDtoList.stream()
                .filter(couponMember -> couponMember.getStatus() == CouponMember.Status.ACTIVE)
                .map(couponMember -> {
                    Long couponId = couponMember.getCoupon().getCouponId();
                    Optional<CouponResponseDto> optionalCouponResponseDto = couponRepository.findCouponById(couponId);

                    if(optionalCouponResponseDto.isEmpty()) {
                        throw new NotFoundCouponException(couponId);
                    }
                    CouponResponseDto couponResponseDto = optionalCouponResponseDto.get();

                    return CouponMemberResponseDto.buildDto(couponMember, couponResponseDto);
                }
        ).collect(Collectors.toList());


        return couponMemberResponseDtoList;
    }

    @Override
    @Transactional
    public CouponMemberResponseDto modifyCouponMemberStatus(Long couponMemberId, CouponMember.Status status) {
        Optional<CouponMember> optionalCouponMember = couponMemberRepository.findById(couponMemberId);

        if(optionalCouponMember.isEmpty()) {
            throw new MemberDoesNotHaveCouponException("Member does not have a coupon.");
        }

        CouponMember couponMember = optionalCouponMember.get();
        CouponResponseDto couponResponseDto = couponRepository.findCouponById(couponMember.getCoupon().getCouponId()).get();

        if(status == couponMember.getStatus()) {
            return CouponMemberResponseDto.buildDto(couponMember, couponResponseDto);
        }
        else {
            if(status == CouponMember.Status.USED) {
                couponMember = couponMember
                        .setStatus(status)
                        .setUsedAtToNow();
                couponMember = couponMemberRepository.save(couponMember);
            }
            else if(status == CouponMember.Status.DESTROYED){
                couponMember = couponMember
                        .setStatus(status)
                        .setDestroyedAtToNow();
                couponMember = couponMemberRepository.save(couponMember);
            }
        }

        return CouponMemberResponseDto.buildDto(couponMember, couponResponseDto);
    }
}
