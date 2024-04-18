package com.nhnacademy.shop.wrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.wrap.controller.WrapController;
import com.nhnacademy.shop.wrap.dto.request.ModifyWrapRequestDto;
import com.nhnacademy.shop.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.shop.wrap.exception.AlreadyExistWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapNameException;
import com.nhnacademy.shop.wrap.service.WrapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WrapController.class)
class WrapControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private WrapService wrapService;
    ObjectMapper objectMapper = new ObjectMapper();
    WrapRequestDto wrapRequestDto;
    ModifyWrapRequestDto modifyWrapRequestDto;
    WrapResponseDto wrapResponseDto;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new WrapController(wrapService)).build();
        wrapRequestDto = WrapRequestDto.builder()
                .wrapName("wrap1").wrapCost(1L).build();
        wrapResponseDto = WrapResponseDto.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
    }
    @Test
    void testGetWraps_ReturnsPageOfWrapResponseDto() throws Exception {

        List<WrapResponseDto> wrapResponseDtos = Collections.singletonList(wrapResponseDto);
        Page<WrapResponseDto> wrapResponseDtoPage = new PageImpl<>(wrapResponseDtos);

        when(wrapService.getWraps(any(Pageable.class))).thenReturn(wrapResponseDtoPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/delivery/wraps")
                        .param("page", "0")
                        .param("size", "10")) // 페이지 크기
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].wrapId").value(wrapResponseDto.getWrapId()))
                .andExpect(jsonPath("$.content[0].wrapName").value(wrapResponseDto.getWrapName()))
                .andExpect(jsonPath("$.content[0].wrapCost").value(wrapResponseDto.getWrapCost()));
    }


    @Test
    void testGetWrapById_ReturnsWrapResponseDto() throws Exception {
        Long wrapId = 1L;
        when(wrapService.getWrapById(wrapId)).thenReturn(wrapResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/delivery/wraps/id/{wrapId}", wrapId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(wrapResponseDto.getWrapId()));
    }
    @Test
    void testGetWrapById_ReturnsNotFoundStatus() throws Exception {
        Long wrapId = 1L;
        when(wrapService.getWrapById(wrapId)).thenThrow(NotFoundWrapException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/delivery/wraps/id/{wrapId}", wrapId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetWrapByName_ReturnsWrapResponseDto() throws Exception{
        String wrapName = "wrap1";
        when(wrapService.getWrapByName(wrapName)).thenReturn(wrapResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/delivery/wraps/name/{wrapName}", wrapName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wrapId").value(wrapResponseDto.getWrapId()));
    }
    @Test
    void testGetWrapByName_ReturnsNotFoundStatus() throws Exception{
        String wrapName = "wrap1";
        when(wrapService.getWrapByName(wrapName)).thenThrow(NotFoundWrapNameException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/delivery/wraps/name/{wrapName}", wrapName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("포장 등록 성공")
    void testSaveWrap_ReturnsCreated() throws Exception {
        when(wrapService.saveWrap(wrapRequestDto)).thenReturn(wrapResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/delivery/wraps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrapRequestDto)))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("포장 등록 실패")
    void testSaveWrap_ReturnsNotFound() throws Exception{

        when(wrapService.saveWrap(any())).thenThrow(AlreadyExistWrapException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/delivery/wraps")
                        .content(objectMapper.writeValueAsString(wrapRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test

    void testModifyWrap_ReturnsWrapResponseDto() throws Exception{
        modifyWrapRequestDto = ModifyWrapRequestDto.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapService.modifyWrap(modifyWrapRequestDto)).thenReturn(wrapResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/delivery/wraps/")
                        .content(objectMapper.writeValueAsString(modifyWrapRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testModifyWarp_ReturnsNotFound() throws Exception{
        modifyWrapRequestDto = ModifyWrapRequestDto.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapService.modifyWrap(any())).thenThrow(NotFoundWrapException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/delivery/wraps/")
                        .content(objectMapper.writeValueAsString(modifyWrapRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteById_ReturnsOkStatus() throws Exception{
        Long wrapId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/delivery/wraps/id/{wrapId}", wrapId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testDeleteById_ReturnsNotFound() throws Exception{
        Long wrapId = 1L;
        doThrow(NotFoundWrapException.class).when(wrapService).deleteWrapById(wrapId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delivery/wraps/id/{wrapId}", wrapId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteByName_ReturnsOkStatus() throws Exception{
        String wrapName = "existingWrapName";

        mockMvc.perform(MockMvcRequestBuilders.delete("/delivery/wraps/name/{wrapName}", wrapName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(wrapService, times(1)).deleteWrapByName(wrapName);
    }
    @Test
    void deleteByName_ReturnsNotFoundStatus() throws Exception {
        String wrapName = "nonExistingWrapName";
        doThrow(NotFoundWrapNameException.class).when(wrapService).deleteWrapByName(wrapName);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delivery/wraps/name/{wrapName}", wrapName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
