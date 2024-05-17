package com.nhnacademy.shop.coupon_member.controller;

import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDtoList;
import com.nhnacademy.shop.coupon_member.service.CouponMemberService;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CouponMember controller Test
 *
 * @Author : 박병휘
 * @Date : 2024/05/11
 */
@WebMvcTest(CouponMemberController.class)
@Import(
        {RedisConfig.class}
)
class CouponMemberControllerTest {
    MockMvc mockMvc;

    @MockBean
    CouponMemberService couponMemberService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CouponMemberController(couponMemberService)).build();
    }

    @Test
    @DisplayName("couponMemberId로 couponId 추출")
    void getCouponIdByCouponMemberId() throws Exception {
        when(couponMemberService.getCouponIdByCouponMemberId(1L)).thenReturn(1L);

        mockMvc.perform(get("/shop/coupon/member/couponMemberId/{couponMemberId}", 1))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").value(1L));
    }

    @Test
    @DisplayName("없는 couponMemberId 호출")
    void testGetCouponIdByCouponMemberId_NotFound() throws Exception {
        when(couponMemberService.getCouponIdByCouponMemberId(1L)).thenThrow(new NotFoundCouponException(1L));

        mockMvc.perform(get("/shop/coupon/member/couponMemberId/{couponMemberId}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("member로 couponMember 호출")
    void testGetCouponMemberByMember() throws Exception {
        Long customerNo = 1L;
        Integer pageSize = 1;
        Integer offset = 10;
        when(couponMemberService.getCouponMemberByMember(any(), any())).thenReturn(CouponMemberResponseDtoList.builder()
                .couponMemberResponseDtoList(Collections.emptyList())
                .maxPage(0)
                .build());

        mockMvc.perform(get("/shop/coupon/member/{customerNo}?pageSize={pageSize}&offset={offset}", customerNo, pageSize, offset))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("없는 member로 couponMember 호출")
    void testGetCouponMemberByMember_MemberNotFound() throws Exception {
        Long customerNo = 1L;
        when(couponMemberService.getCouponMemberByMember(anyLong(), any())).thenThrow(new MemberNotFoundException());

        mockMvc.perform(get("/shop/coupon/member/{customerNo}?pageSize=1&offset=10", customerNo))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("없는 couponId가 있는 경우 couponMember 호출")
    void testGetCouponMemberByMember_NotFoundCoupon() throws Exception {
        Long customerNo = 1L;
        when(couponMemberService.getCouponMemberByMember(anyLong(), any())).thenThrow(new NotFoundCouponException(1L));

        mockMvc.perform(get("/shop/coupon/member/{customerNo}?pageSize=1&offset=10", customerNo))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("couponMember 생성")
    void testCreateCouponMember() throws Exception {
        Long customerNo = 1L;
        Long couponId = 1L;
        when(couponMemberService.createCouponMember(any(), any())).thenReturn(CouponMemberResponseDto.builder()
                        .customerNo(1L)
                        .couponId(1L)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(post("/shop/coupon/member/{customerNo}/{couponId}", customerNo, couponId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("member가 없는 경우 couponMember 생성")
    void testCreateCouponMember_MemberNotFound() throws Exception {
        Long customerNo = 1L;
        Long couponId = 1L;
        when(couponMemberService.createCouponMember(anyLong(), anyLong())).thenThrow(new MemberNotFoundException());

        mockMvc.perform(post("/shop/coupon/member/{customerNo}/{couponId}", customerNo, couponId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("coupon이 없는 경우 couponMember 생성")
    void testCreateCouponMember_NotFoundCoupon() throws Exception {
        Long customerNo = 1L;
        Long couponId = 1L;
        when(couponMemberService.createCouponMember(anyLong(), anyLong())).thenThrow(new NotFoundCouponException(1L));

        mockMvc.perform(post("/shop/coupon/member/{customerNo}/{couponId}", customerNo, couponId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("couponMember status 변경")
    void testModifyCouponMember() throws Exception {
        Long couponMemberId = 1L;
        when(couponMemberService.modifyCouponMemberStatus(any(), any())).thenReturn(CouponMemberResponseDto.builder()
                        .customerNo(1L)
                        .couponId(1L)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        mockMvc.perform(put("/shop/coupon/member/{couponMemberId}", couponMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("member가 없는 경우 couponMember status 변경")
    void testModifyCouponMember_MemberNotFound() throws Exception {
        Long couponMemberId = 1L;
        when(couponMemberService.modifyCouponMemberStatus(anyLong(), any())).thenThrow(new MemberNotFoundException());

        mockMvc.perform(put("/shop/coupon/member/{couponMemberId}", couponMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ACTIVE\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("couponMember 없는 경우 couponMember status 변경")
    void testModifyCouponMember_NotFoundCoupon() throws Exception {
        Long couponMemberId = 1L;
        when(couponMemberService.modifyCouponMemberStatus(anyLong(), any())).thenThrow(new NotFoundCouponException(1L));

        mockMvc.perform(put("/shop/coupon/member/{couponMemberId}", couponMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ACTIVE\"}"))
                .andExpect(status().isNotFound());
    }
}
