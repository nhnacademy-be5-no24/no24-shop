package com.nhnacademy.shop.address.service.impl;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AddressService 구현체
 *
 * @Author : jinjiwon
 * @Date : 28/03/2024
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    // 주소 조회
    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddresses(Long customerNo) {
        List<Address> addresses = addressRepository.findByCustomerNo(customerNo);
        return AddressMapper.addressToAddressResponseDtoList(addresses);
    }
}
