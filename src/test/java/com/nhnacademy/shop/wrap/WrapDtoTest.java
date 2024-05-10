package com.nhnacademy.shop.wrap;

import com.nhnacademy.shop.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDtoList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class WrapDtoTest {

    @Test
    void testDto(){
        WrapResponseDto wrapResponseDto = WrapResponseDto.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        WrapResponseDto wrapResponseDto2 = WrapResponseDto.builder()
                .wrapId(2L).wrapName("wrap2").wrapCost(2L).build();

        List<WrapResponseDto> wrapResponseDtos = List.of(wrapResponseDto, wrapResponseDto2);
        WrapResponseDtoList wrapResponseDtoList = new WrapResponseDtoList(wrapResponseDtos);

        assertEquals("wrap2", wrapResponseDtoList.getWrapResponseDtos().get(1).getWrapName());

    }
}
