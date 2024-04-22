package com.nhnacademy.shop.wrap.dto.response;

import com.nhnacademy.shop.wrap.domain.Wrap;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 포장 기본정보를 반환하기 위한 response dto 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-29
 **/
@Getter
@NoArgsConstructor
@Builder
public class WrapResponseDto {
    private Long wrapId;
    private String wrapName;
    private Long wrapCost;

    public WrapResponseDto(Wrap wrap){
        this.wrapId = wrap.getWrapId();
        this.wrapName = wrap.getWrapName();
        this.wrapCost = wrap.getWrapCost();
    }

    public WrapResponseDto(Long wrapId, String wrapName, Long wrapCost) {
        this.wrapId = wrapId;
        this.wrapName = wrapName;
        this.wrapCost = wrapCost;
    }
}
