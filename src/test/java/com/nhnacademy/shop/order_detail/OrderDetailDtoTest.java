package com.nhnacademy.shop.order_detail;

import com.nhnacademy.shop.order_detail.dto.OrderDetailDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class OrderDetailDtoTest {
    @Test
    void testDto(){

        List<OrderDetailDto.WrapDto> wrapInfoDtos = new ArrayList<>();
        OrderDetailDto.WrapDto wrapInfoDto = OrderDetailDto.WrapDto.builder()
                .wrapId(1L)
                .quantity(1L)
                .build();
        wrapInfoDtos.add(wrapInfoDto);

        OrderDetailDto dto = OrderDetailDto.builder()
                .bookIsbn("bookisbn")
                .quantity(1L)
                .price(1L)
                .wraps(wrapInfoDtos)
                .build();
        assertEquals(1L, dto.getPrice());
    }
}
