package com.nhnacademy.shop.address.service;

import com.nhnacademy.shop.address.dto.request.AddressModifyRequestDto;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;

import java.util.List;

/**
 * Address service
 *
 * @Author : jinjiwon
 * @Date : 28/03/2024
 */
public interface AddressService {
    // 주소 조회
    List<AddressResponseDto> getAddresses(Long customerNo);

    // 주소 등록

    // 주소 수정
    AddressResponseDto modifyAddress(Long addressId, AddressModifyRequestDto addressModifyRequestDto);

    // 주소 삭제
    void deleteAddress(Long addressId);
}
