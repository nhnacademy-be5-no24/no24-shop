package com.nhnacademy.shop.address.controller;

import com.nhnacademy.shop.address.dto.request.AddressCreateRequestDto;
import com.nhnacademy.shop.address.dto.request.AddressModifyRequestDto;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;
import com.nhnacademy.shop.address.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Address Controller
 *
 * @Author : jinjiwon
 * @Date : 29/03/2024
 */
@RestController
@RequestMapping("/shop/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // 주소 조회
    @GetMapping("/{customerNo}")
    public List<AddressResponseDto> getAddresses(@PathVariable Long customerNo) {
        return addressService.getAddresses(customerNo);
    }

    // 주소 등록
    @PostMapping("/create")
    public ResponseEntity<AddressResponseDto> saveAddress(@RequestBody AddressCreateRequestDto addressCreateRequestDto) {
        AddressResponseDto addressResponseDto = addressService.saveAddress(addressCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponseDto);
    }

    // 주소 수정
    @PutMapping("/modify/{addressId}")
    public AddressResponseDto modifyAddress(@PathVariable Long addressId, @RequestBody AddressModifyRequestDto addressModifyRequestDto) {
        return addressService.modifyAddress(addressId, addressModifyRequestDto);
    }

    // 주소 삭제
    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
