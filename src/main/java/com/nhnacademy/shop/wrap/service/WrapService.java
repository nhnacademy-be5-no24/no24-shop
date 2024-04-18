package com.nhnacademy.shop.wrap.service;


import com.nhnacademy.shop.wrap.dto.request.ModifyWrapRequestDto;
import com.nhnacademy.shop.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 포장(Wrap) service
 *
 * @author : 박동희
 * @date : 2024-03-29
 *
 **/
public interface WrapService {
    /**
     * 포장 전체 조회를 위한 method.
     *
     * @return WrapResponDto 리스트를 반환.
     */
    Page<WrapResponseDto> getWraps(Pageable pageable);


    /**
     * 포장 id로 단건 조회를 위한 method.
     *
     * @param wrapId 조회할 포장 아이디 입니다.
     * @return WrapResponseDto 포장 정보가 반환됩니다.
     */
    WrapResponseDto getWrapById(Long wrapId);


    /**
     * 포장 이름으로 단건 조회를 위한 method.
     *
     * @param wrapName 조회할 포장이름 입니다.
     * @return WrapResponseDto 포장 정보가 반환됩니다.
     */
    WrapResponseDto getWrapByName(String wrapName);


    /**
     * 포장를 저장하기 위한 method.
     *
     * @param wrappingRequestDto 저장하기 포장 정보 입니다.
     * @return WrapResponseDto 포장 정보가 반환됩니다.
     */
    WrapResponseDto saveWrap(WrapRequestDto wrappingRequestDto);

    /**
     * 포장를 수정하기 위한 method.
     *
     * @param modifyWrapRequestDto 수정하기 포장 정보 입니다.
     * @return WrapResponseDto 포장 정보가 반환됩니다.
     */
    WrapResponseDto modifyWrap(ModifyWrapRequestDto modifyWrapRequestDto);

    /**
     * 포장 아이디를 통해 포장을 삭제하기 위한 method.
     *
     * @param wrapId 포장 아디디 입니다.
     */
    void deleteWrapById(Long wrapId);

    /**
     * 포장 이름를 통해 포장을 삭제하기 위한 method.
     *
     * @param wrapName 포장 아디디 입니다.
     */
    void deleteWrapByName(String wrapName);

}
