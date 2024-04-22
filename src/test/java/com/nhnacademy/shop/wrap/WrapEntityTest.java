package com.nhnacademy.shop.wrap;


import com.nhnacademy.shop.wrap.domain.Wrap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class WrapEntityTest {
    @Test
    void testWrapEntityGetterTest(){

        Wrap wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("wrapName")
                .wrapCost(1L)
                .build();
        Long wrapId = wrap.getWrapId();
        String wrapName = wrap.getWrapName();
        Long wrapCost = wrap.getWrapCost();

        assertEquals(Long.valueOf(1L), wrapId);
        assertEquals(String.valueOf("wrapName"), wrapName);
        assertEquals(Long.valueOf(1L), wrapCost);
    }
}
