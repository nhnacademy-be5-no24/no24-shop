package com.nhnacademy.shop.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.exception.IllegalFormCouponRequestException;
import com.nhnacademy.shop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.shop.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/04/18
 */
@WebMvcTest(CouponController.class)
@Import(
        {RedisConfig.class}
)
class CouponControllerTest {
    MockMvc mockMvc;

    @MockBean
    CouponService couponService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CouponController(couponService)).build();
    }


    @Test
    public void testGetCoupons() throws Exception {
        Page<CouponResponseDto> couponPage = createCouponPage();
        when(couponService.getAllCoupons(anyInt(), anyInt())).thenReturn(couponPage);

        mockMvc.perform(get("/shop/coupon")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.couponResponseDtoList[0].couponId").value(1));
    }

    @Test
    public void testGetCoupon() throws Exception {
        CouponResponseDto couponResponseDto = createCouponResponseDto();
        when(couponService.getCouponById(anyLong())).thenReturn(couponResponseDto);

        mockMvc.perform(get("/shop/coupon/{couponId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.couponId").value(1));
    }

    @Test
    public void testCreateCoupon() throws Exception {
        CouponRequestDto couponRequestDto = createCouponRequestDto();
        CouponResponseDto couponResponseDto = createCouponResponseDto();
        when(couponService.saveCoupon(any())).thenReturn(couponResponseDto);

        mockMvc.perform(post("/shop/coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(couponRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.couponId").value(1));
    }

    @Test
    public void testGetCouponsByContainingName() throws Exception {
        Page<CouponResponseDto> couponPage = createCouponPage();
        when(couponService.getCouponsByContainingName(anyString(), anyInt(), anyInt())).thenReturn(couponPage);

        mockMvc.perform(get("/shop/coupon/search/{couponName}", "TestCoupon")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].couponId").value(1));
    }

    @Test
    public void testGetCouponsByCategoryId() throws Exception {
        Page<CouponResponseDto> couponPage = createCouponPage();
        when(couponService.getCategoryCoupons(anyLong(), anyInt(), anyInt())).thenReturn(couponPage);

        mockMvc.perform(get("/shop/coupon/category/{categoryId}", 1)
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].couponId").value(1));
    }

    @Test
    public void testGetCouponsByBookIsbn() throws Exception {
        Page<CouponResponseDto> couponPage = createCouponPage();
        when(couponService.getBookCoupons(anyString(), anyInt(), anyInt())).thenReturn(couponPage);

        mockMvc.perform(get("/shop/coupon/book/{bookIsbn}", "1234567890123")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].couponId").value(1));
    }

    @Test
    public void testCreateCoupons() throws Exception {
        CouponRequestDto couponRequestDto = createCouponRequestDto();
        CouponResponseDto couponResponseDto = createCouponResponseDto();
        when(couponService.saveCoupon(any())).thenReturn(couponResponseDto);

        mockMvc.perform(post("/shop/coupon/create/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Collections.singletonList(couponRequestDto))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].couponId").value(1));
    }

    @Test
    public void testDeleteCoupon() throws Exception {
        mockMvc.perform(delete("/shop/coupon/delete/{couponId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCouponsNotFound() throws Exception {
        when(couponService.getAllCoupons(anyInt(), anyInt())).thenThrow(new NotFoundCouponException(1L));

        mockMvc.perform(get("/shop/coupon/")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCouponNotFound() throws Exception {
        when(couponService.getCouponById(anyLong())).thenThrow(new NotFoundCouponException(1L));

        mockMvc.perform(get("/shop/coupon/{couponId}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCouponNotFound() throws Exception {
        doThrow(new NotFoundCouponException(1L)).when(couponService).deleteCoupon(anyLong());

        mockMvc.perform(delete("/shop/coupon/delete/{couponId}", 1))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetCouponsByContainingNameBadRequest() throws Exception {
        doThrow(new IllegalArgumentException()).when(couponService).getCouponsByContainingName(anyString(), anyInt(), anyInt());

        mockMvc.perform(get("/shop/coupon/search/{couponName}", "TestCoupon")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCouponsByCategoryIdBadRequest() throws Exception {
        doThrow(new IllegalArgumentException()).when(couponService).getCategoryCoupons(anyLong(), anyInt(), anyInt());

        mockMvc.perform(get("/shop/coupon/category/{categoryId}", 1)
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCouponsByBookIsbnBadRequest() throws Exception {
        doThrow(new IllegalArgumentException()).when(couponService).getBookCoupons(anyString(), anyInt(), anyInt());

        mockMvc.perform(get("/shop/coupon/book/{bookIsbn}", "1234567890123")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateCouponsBadRequest() throws Exception {
        doThrow(new IllegalFormCouponRequestException()).when(couponService).saveCoupon(any());

        mockMvc.perform(post("/shop/coupon/create/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Collections.singletonList(createCouponRequestDto()))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteCouponBadRequest() throws Exception {
        doThrow(new NotFoundCouponException(1L)).when(couponService).deleteCoupon(anyLong());

        mockMvc.perform(delete("/shop/coupon/delete/{couponId}", 1))
                .andExpect(status().isNotFound());
    }

    // Helper methods to create mock objects
    private Page<CouponResponseDto> createCouponPage() {
        CouponResponseDto couponResponseDto = createCouponResponseDto();
        return new PageImpl<>(Collections.singletonList(couponResponseDto));
    }

    private CouponResponseDto createCouponResponseDto() {
        CouponResponseDto couponResponseDto = new CouponResponseDto();
        couponResponseDto.setCouponId(1L);
        couponResponseDto.setCouponName("Test Coupon");
        // Set other fields as needed
        return couponResponseDto;
    }

    private CouponRequestDto createCouponRequestDto() {
        CouponRequestDto couponRequestDto = new CouponRequestDto();
        couponRequestDto.setCouponName("Test Coupon");
        // Set other fields as needed
        return couponRequestDto;
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}