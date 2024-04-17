package com.nhnacademy.shop.wrap;


import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.dto.request.ModifyWrapRequestDto;
import com.nhnacademy.shop.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.shop.wrap.exception.AlreadyExistWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapNameException;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
import com.nhnacademy.shop.wrap.service.WrapService;
import com.nhnacademy.shop.wrap.service.impl.WrapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Transactional
@WebAppConfiguration
class WrapServiceTest {
    @Mock
    private final WrapRepository wrapRepository = mock(WrapRepository.class);
    private WrapService wrapService;
    @BeforeEach
    void setup(){
        wrapService = new WrapServiceImpl(wrapRepository);
    }
    @Test
    void testGetWraps(){
        List<Wrap> wraps = new ArrayList<>();
        Wrap wrap1 = Wrap.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        Wrap wrap2 = Wrap.builder()
                .wrapId(2L).wrapName("wrap2").wrapCost(2L).build();
        wraps.add(wrap1);
        wraps.add(wrap2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Wrap> wrapsPage = new PageImpl<>(wraps, pageable, wraps.size());

        when(wrapRepository.findAll(pageable)).thenReturn(wrapsPage);
        Page<WrapResponseDto> results = wrapService.getWraps(pageable);

        assertEquals(2, results.getTotalElements());
        assertEquals(1L, results.getContent().get(0).getWrapId());
        assertEquals("wrap1", results.getContent().get(0).getWrapName());
        assertEquals(1L, results.getContent().get(0).getWrapCost());
        assertEquals(2L, results.getContent().get(1).getWrapId());
        assertEquals("wrap2", results.getContent().get(1).getWrapName());
        assertEquals(2L, results.getContent().get(1).getWrapCost());
    }

    @Test
    void testGetWrapById_Success(){
        Wrap wrap = Wrap.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapRepository.findById(1L)).thenReturn(Optional.of(wrap));

        WrapResponseDto result = wrapService.getWrapById(1L);

        assertEquals(1L, result.getWrapId());
        assertEquals("wrap1", result.getWrapName());
        assertEquals(1L, result.getWrapCost());
    }
    @Test
    void testGetWarpById_Exception(){
        Long wrapId = 1L;
        when(wrapRepository.findById(wrapId)).thenReturn(Optional.empty());

        assertThrows(NotFoundWrapException.class, () -> wrapService.getWrapById(wrapId));
    }
    @Test
    void testGetWrapByName_Success(){
        Wrap wrap = Wrap.builder()
                .wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapRepository.findWrapByWrapName("wrap1")).thenReturn(Optional.of(wrap));

        WrapResponseDto result = wrapService.getWrapByName("wrap1");

        assertEquals(1L, result.getWrapId());
        assertEquals("wrap1", result.getWrapName());
        assertEquals(1L, result.getWrapCost());
    }
    @Test
    void testGetWarpByName_Exception(){
        String wrapName = "wrap1";
        when(wrapRepository.findWrapByWrapName(wrapName)).thenReturn(Optional.empty());

        assertThrows(NotFoundWrapNameException.class, () -> wrapService.getWrapByName(wrapName));
    }
    @Test
    void testSaveWrap_Success(){
        WrapRequestDto requestDto = new WrapRequestDto("Wrap1", 100L);
        when(wrapRepository.findWrapByWrapName("Wrap1")).thenReturn(Optional.empty());
        when(wrapRepository.save(any(Wrap.class))).thenReturn(new Wrap(1L, "Wrap1", 100L));

        WrapResponseDto result = wrapService.saveWrap(requestDto);

        assertEquals("Wrap1", result.getWrapName());
        assertEquals(100L, result.getWrapCost());
    }
    @Test
    void testSaveWrap_Exception(){
        WrapRequestDto requestDto = new WrapRequestDto("wrap1", 100L);
        when(wrapRepository.findWrapByWrapName("wrap1")).thenReturn(Optional.of(new Wrap()));

        assertThrows(AlreadyExistWrapException.class, () -> wrapService.saveWrap(requestDto));
    }

    @Test
    void testModifyWrap_Success(){
        ModifyWrapRequestDto modifyWrapRequestDto = new ModifyWrapRequestDto(1L, "wrap2", 2L);
        Wrap wrap = Wrap.builder().wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapRepository.findById(1L)).thenReturn(Optional.of(wrap));
        when(wrapRepository.save(any())).thenReturn(new Wrap(1L, "wrap2", 2L));

        WrapResponseDto result = wrapService.modifyWrap(modifyWrapRequestDto);

        assertEquals(1L, result.getWrapId());
        assertEquals("wrap2", result.getWrapName());
        assertEquals(2L, result.getWrapCost());
    }

    @Test
    void testModifyWrap_Exception(){
        ModifyWrapRequestDto modifyWrapRequestDto = new ModifyWrapRequestDto(1L, "wrap2", 2L);
        when(wrapRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundWrapException.class, () -> wrapService.modifyWrap(modifyWrapRequestDto));
    }

    @Test
    void deleteWrapById_Success(){
        Wrap wrap = Wrap.builder().wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapRepository.findById(any())).thenReturn(Optional.of(wrap));

        wrapService.deleteWrapById(1L);
        verify(wrapRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteWrapById_Exception(){
        Long wrapId = 1L;
        when(wrapRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundWrapException.class, () -> wrapService.deleteWrapById(wrapId));
    }
    @Test
    void deleteWrapByName_Success(){
        Wrap wrap = Wrap.builder().wrapId(1L).wrapName("wrap1").wrapCost(1L).build();
        when(wrapRepository.findWrapByWrapName(any())).thenReturn(Optional.of(wrap));

        wrapService.deleteWrapByName("wrap1");
        verify(wrapRepository, times(1)).delete(any(Wrap.class));
    }
    @Test
    void deleteWrapByName_Exception(){
        String wrapName = "wrap1";
        when(wrapRepository.findWrapByWrapName(wrapName)).thenReturn(Optional.empty());
        assertThrows(NotFoundWrapNameException.class, () -> wrapService.deleteWrapByName(wrapName));
    }
}
