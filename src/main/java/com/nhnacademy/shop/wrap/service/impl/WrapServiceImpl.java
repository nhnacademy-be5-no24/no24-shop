package com.nhnacademy.shop.wrap.service.impl;


import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.dto.request.ModifyWrapRequestDto;
import com.nhnacademy.shop.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.shop.wrap.exception.AlreadyExistWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapNameException;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
import com.nhnacademy.shop.wrap.service.WrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 포장 서비스의 구현체입니다.
 *
 * @author : 박동희
 * @date : 2024-03-29
 **/
@Service
@RequiredArgsConstructor
public class WrapServiceImpl implements WrapService {
    private final WrapRepository wrapRepository;

    /**
     * 포장 전체 조회를 위한 method.
     *
     * @return WrapResponDto 리스트를 반환.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WrapResponseDto> getWraps(Pageable pageable) {
        Page<Wrap> wrapsPage = wrapRepository.findAll(pageable);
        return wrapsPage.map(wrap -> new WrapResponseDto(wrap.getWrapId(), wrap.getWrapName(), wrap.getWrapCost()));
    }

    @Override
    @Transactional(readOnly = true)
    public WrapResponseDto getWrapById(Long wrapId) {
        Optional<Wrap> optionalWrapping = wrapRepository.findById(wrapId);
        if(optionalWrapping.isEmpty()) {
            throw new NotFoundWrapException(wrapId);
        }
        return new WrapResponseDto(optionalWrapping.get());
    }

    @Override
    @Transactional(readOnly = true)
    public WrapResponseDto getWrapByName(String wrapName) {
        Optional<Wrap> optionalWrapping = wrapRepository.findWrapByWrapName(wrapName);
        if(optionalWrapping.isEmpty()) {
            throw new NotFoundWrapNameException(wrapName);
        }
        return new WrapResponseDto(optionalWrapping.get());
    }

    @Override
    @Transactional
    public WrapResponseDto saveWrap(WrapRequestDto wrappingRequestDto) {
        String wrapName = wrappingRequestDto.getWrapName();
        if (wrapRepository.findWrapByWrapName(wrapName).isPresent()) {
            throw new AlreadyExistWrapException(wrapName);
        }
        Wrap wrap = new Wrap(null, wrappingRequestDto.getWrapName(), wrappingRequestDto.getWrapCost());
        Wrap createdWrap = wrapRepository.save(wrap);
        return new WrapResponseDto(createdWrap);
    }

    @Override
    @Transactional
    public WrapResponseDto modifyWrap(ModifyWrapRequestDto modifyWrappingRequestDto) {
        Optional<Wrap> optionalWrap = wrapRepository.findById(modifyWrappingRequestDto.getWrapId());
        if (optionalWrap.isEmpty()) {
            throw new NotFoundWrapException(modifyWrappingRequestDto.getWrapId());
        }
        Wrap wrap = Wrap.builder()
                .wrapId(optionalWrap.get().getWrapId())
                .wrapName(modifyWrappingRequestDto.getWrapName())
                .wrapCost(modifyWrappingRequestDto.getWrapCost())
                .build();

        Wrap updatedWrap = wrapRepository.save(wrap);
        return new WrapResponseDto(updatedWrap);
    }

    @Override
    @Transactional
    public void deleteWrapById(Long wrapId) {
        if (wrapRepository.findById(wrapId).isEmpty()){
            throw new NotFoundWrapException(wrapId);
        }
        wrapRepository.deleteById(wrapId);
    }

    @Override
    public void deleteWrapByName(String wrapName) {
        Optional<Wrap> optionalWrap = wrapRepository.findWrapByWrapName(wrapName);
        if (optionalWrap.isEmpty()) {
            throw new NotFoundWrapNameException(wrapName);
        }
        wrapRepository.delete(optionalWrap.get());
    }
}
