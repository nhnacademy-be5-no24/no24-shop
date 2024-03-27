package com.nhnacademy.shop.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.dto.request.PointRequestDto;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import com.nhnacademy.shop.point.service.PointLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Point RestController 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-07
 */
@WebMvcTest(value = PointLogController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PointLogService pointLogService;
    private ObjectMapper objectMapper = new ObjectMapper();
    PointLog pointLog;
    Member member;
    Pageable pageable;
    Integer pageSize;
    Integer offset;
    PointResponseDto pointResponseDto;
    PointRequestDto pointRequestDto;
    Page<PointResponseDto> pointPage;
    LocalDateTime startDate;
    LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "abc111", null, 1L);
        pointLog = new PointLog(1L, member, 1L, "리뷰 작성", 500, false,
                LocalDateTime.of(2024, 4, 5, 0,0,0));
        pageSize = 0;
        offset = 10;
        pageable = PageRequest.of(pageSize, offset);
        pointResponseDto = new PointResponseDto(1L, member.getCustomerNo(), 1L, "리뷰 작성", 500, false,
                LocalDateTime.of(2024, 4, 5, 0, 0, 0));
        pointRequestDto = new PointRequestDto(member.getCustomerNo(), 1L, "리뷰 작성", 500, false,
                LocalDateTime.of(2024, 4, 5, 0, 0, 0));
        pointPage = new PageImpl<>(List.of(pointResponseDto), pageable, 1);
        startDate = LocalDateTime.of(2024, 4, 3, 0, 0, 0);
        endDate = LocalDateTime.of(2024, 4, 6, 0, 0, 0);
    }

    @Test
    @Order(1)
    @DisplayName(value = "리뷰 단건 조회 성공")
    void getReviewTest_Success() {
        when(pointLogService.getPointLogs(pageSize, offset)).thenReturn(pointPage);
        try {
            mockMvc.perform(get("/shop/points")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
