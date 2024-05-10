package com.nhnacademy.shop.grade.controller;

import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.dto.request.GradeRequestDto;
import com.nhnacademy.shop.grade.dto.response.GradeResponseDto;
import com.nhnacademy.shop.grade.exception.AlreadyExistsGradeException;
import com.nhnacademy.shop.grade.exception.NotFoundGradeException;
import com.nhnacademy.shop.grade.service.GradeService;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Grade RestController 테스트
 *
 * @author : 박병휘
 * @date : 2024-05-10
 */
@WebMvcTest(value = GradeController.class)
@Import(
        {RedisConfig.class}
)
class GradeControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private GradeService gradeService;
    @MockBean
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GradeController(gradeService, memberRepository)).build();
    }

    @Test
    @DisplayName(value="전체 등급 조회")
    void getAllGrades() throws Exception{
        when(gradeService.findAllGrades()).thenReturn(List.of(
                GradeResponseDto.builder()
                        .gradeId(1L)
                        .gradeName("NORMAL")
                        .gradeNameKor("일반")
                        .accumulateRate(1L)
                        .tenPercentCoupon(0)
                        .twentyPercentCoupon(0)
                        .requiredPayment(0L)
                        .build(),
                GradeResponseDto.builder()
                        .gradeId(2L)
                        .gradeName("ROYAL")
                        .gradeNameKor("로얄")
                        .accumulateRate(2L)
                        .tenPercentCoupon(1)
                        .twentyPercentCoupon(0)
                        .requiredPayment(100000L)
                        .build()
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/grade/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeResponseDtoList[0].gradeId").value(1));
    }

    @Test
    @DisplayName(value="등급 Id로 조회")
    void getGradeById() throws Exception{
        when(gradeService.findGradeById(1L)).thenReturn(GradeResponseDto.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .gradeNameKor("일반")
                .accumulateRate(1L)
                .tenPercentCoupon(0)
                .twentyPercentCoupon(0)
                .requiredPayment(0L)
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/grade/id/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeId").value(1));
    }

    @Test
    @DisplayName(value="존재하지 않는 등급 Id로 조회")
    void getGradeByDoesNotFoundId() throws Exception{
        when(gradeService.findGradeById(1L)).thenThrow(NotFoundGradeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/grade/id/{gradeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName(value="CustomerNo로 조회")
    void getGradeByCustomerNo() throws Exception {
        Member member = Member.builder()
                .customerNo(1L)
                .grade(new Grade(1L, "A", "에이", 10L, 1, 2, 100L))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/grade/customer/{customerNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeId").value(1));
    }

    @Test
    @DisplayName(value="CustomerNo로 조회")
    void getGradeByDoesNotExistCustomerNo() throws Exception {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/grade/customer/{customerNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName(value="member가 없는 경우")
    void getGradeByCustomerNo_NotFound() throws Exception {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/grade/customer/{customerNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName(value="Grade 생성")
    void createGrade() throws Exception {
        GradeRequestDto requestDto = GradeRequestDto.builder()
                .gradeName("A")
                .gradeNameKor("에이")
                .accumulateRate(10L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(2)
                .requiredPayment(100L)
                .build();
        GradeResponseDto responseDto = new GradeResponseDto(1L, "A", "에이", 10L, 1, 2, 100L);
        when(gradeService.saveGrade(any())).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/grade/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gradeName\":\"A\",\"gradeNameKor\":\"에이\",\"accumulateRate\":10,\"tenPercentCoupon\":1,\"twentyPercentCoupon\":2,\"requiredPayment\":100}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeId").value(1));
    }


    @Test
    @DisplayName(value="잘못된 grade 생성")
    void createIllegalGrade() throws Exception {
        when(gradeService.saveGrade(any())).thenThrow(AlreadyExistsGradeException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/grade/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gradeName\":\"A\",\"gradeNameKor\":\"에이\",\"accumulateRate\":10,\"tenPercentCoupon\":1,\"twentyPercentCoupon\":2,\"requiredPayment\":100}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName(value="등급 업데이트")
    void updateGrade() throws Exception {
        GradeRequestDto requestDto = GradeRequestDto.builder()
                .gradeName("A")
                .gradeNameKor("에이")
                .accumulateRate(10L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(2)
                .requiredPayment(100L)
                .build();
        GradeResponseDto responseDto = new GradeResponseDto(1L, "A", "에이", 10L, 1, 2, 100L);
        when(gradeService.updateGrade(any())).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/shop/grade/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gradeName\":\"A\",\"gradeNameKor\":\"에이\",\"accumulateRate\":10,\"tenPercentCoupon\":1,\"twentyPercentCoupon\":2,\"requiredPayment\":100}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeId").value(1));
    }

    @Test
    @DisplayName(value="잘못된 등급 업데이트")
    void updateIllegalGrade() throws Exception {
        when(gradeService.updateGrade(any())).thenThrow(NotFoundGradeException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/shop/grade/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gradeName\":\"A\",\"gradeNameKor\":\"에이\",\"accumulateRate\":10,\"tenPercentCoupon\":1,\"twentyPercentCoupon\":2,\"requiredPayment\":100}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
