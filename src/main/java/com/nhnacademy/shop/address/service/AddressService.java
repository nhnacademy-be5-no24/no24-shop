package com.nhnacademy.shop.address.service;

import com.nhnacademy.shop.address.dto.request.AddressDeleteRequestDto;
import com.nhnacademy.shop.address.dto.request.AddressRequestDto;
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
}
