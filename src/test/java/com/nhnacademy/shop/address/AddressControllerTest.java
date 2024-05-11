package com.nhnacademy.shop.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.shop.address.controller.AddressController;
import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.dto.request.AddressCreateRequestDto;
import com.nhnacademy.shop.address.dto.request.AddressModifyRequestDto;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;
import com.nhnacademy.shop.address.service.AddressService;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Address RestController Test
 *
 * @Author : jinjiwon
 * @Date : 02/04/2024
 */
@WebMvcTest(AddressController.class)
@Import(
        {RedisConfig.class}
)
class AddressControllerTest {
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    AddressService addressService;

    Address address;
    Customer customer;
    Grade grade;
    Member member;
    AddressResponseDto addressResponseDto;
    AddressCreateRequestDto addressCreateRequestDto;
    AddressModifyRequestDto addressModifyRequestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AddressController(addressService)).build();
        objectMapper.registerModule(new JavaTimeModule());
        customer = Customer.builder()
                .customerNo(1L)
//                .customerId("abcd")
                .customerPassword("1234")
                .customerName("홍길동")
                .customerPhoneNumber("01051745441")
                .customerEmail("jin@naver.com")
                .customerBirthday(LocalDate.of(2001, 2, 21))
                .customerRole("ROLE_MEMBER")
                .build();

        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("silver")
                .accumulateRate(30L)
                .build();

        member = Member.builder()
                .customerNo(1L)
                .customer(customer)
                .memberId("abcd")
                .lastLoginAt(LocalDateTime.of(2024, 4, 4, 10, 42))
                .grade(grade)
                .role("ROLE_MEMBER")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        address = Address.builder()
                .addressId(1L)
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 정보화본부 교육관 1303호")
                .isDefault(false)
                .build();

        addressResponseDto = AddressResponseDto.builder()
                .addressId(1L)
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 정보화본부 교육관 1303호")
                .isDefault(false)
                .build();

        addressCreateRequestDto = AddressCreateRequestDto.builder()
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 정보화본부 교육관 1303호")
                .isDefault(false)
                .customerNo(member.getCustomerNo())
                .build();

        addressModifyRequestDto = AddressModifyRequestDto.builder()
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 기숙사 11동")
                .isDefault(true)
                .build();
    }

    @Test
    @DisplayName(value = "주소 등록")
    void testSaveAddress_ReturnAddressResponseDto() throws Exception {
        when(addressService.saveAddress(addressCreateRequestDto)).thenReturn(addressResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/address/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressCreateRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").value(addressResponseDto.getAlias()))
                .andExpect(jsonPath("$.receiver_name").value(addressResponseDto.getReceiverName()))
                .andExpect(jsonPath("$.receiver_phone_number").value(addressResponseDto.getReceiverPhoneNumber()))
                .andExpect(jsonPath("$.zipcode").value(addressResponseDto.getZipcode()))
                .andExpect(jsonPath("$.address").value(addressResponseDto.getAddress()))
                .andExpect(jsonPath("$.address_detail").value(addressResponseDto.getAddressDetail()))
                .andExpect(jsonPath("$.is_default").value(addressResponseDto.getIsDefault()));
    }

    @Test
    @DisplayName(value = "주소 조회 by customerNo")
    void testGetAddresses_ReturnAddressResponseDtoList() throws Exception {
        Long customerNo = 1L;
        List<AddressResponseDto> addressResponseList = Collections.singletonList(addressResponseDto);

        when(addressService.getAddresses(customerNo)).thenReturn(addressResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/address/customer/{customerNo}", customerNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..[0]['alias']").value(addressResponseDto.getAlias()))
                .andExpect(jsonPath("$..[0]['receiver_name']").value(addressResponseDto.getReceiverName()))
                .andExpect(jsonPath("$..[0]['receiver_phone_number']").value(addressResponseDto.getReceiverPhoneNumber()))
                .andExpect(jsonPath("$..[0]['zipcode']").value(addressResponseDto.getZipcode()))
                .andExpect(jsonPath("$..[0]['address']").value(addressResponseDto.getAddress()))
                .andExpect(jsonPath("$..[0]['address_detail']").value(addressResponseDto.getAddressDetail()))
                .andExpect(jsonPath("$..[0]['is_default']").value(addressResponseDto.getIsDefault()));
    }

    @Test
    @DisplayName(value = "주소 조회 by addressId")
    void testGetAddress_ReturnAddressResponseDto() throws Exception {
        Long addressId = 1L;

        when(addressService.getAddress(addressId)).thenReturn(addressResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/address/{addressId}", addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").value(addressResponseDto.getAlias()))
                .andExpect(jsonPath("$.receiver_name").value(addressResponseDto.getReceiverName()))
                .andExpect(jsonPath("$.receiver_phone_number").value(addressResponseDto.getReceiverPhoneNumber()))
                .andExpect(jsonPath("$.zipcode").value(addressResponseDto.getZipcode()))
                .andExpect(jsonPath("$.address").value(addressResponseDto.getAddress()))
                .andExpect(jsonPath("$.address_detail").value(addressResponseDto.getAddressDetail()))
                .andExpect(jsonPath("$.is_default").value(addressResponseDto.getIsDefault()));


    }

    @Test
    @DisplayName(value = "주소 수정")
    void testModifyAddress_ReturnAddressResponseDto() throws Exception {
        Long addressId = 1L;

        when(addressService.modifyAddress(addressId, addressModifyRequestDto)).thenReturn(addressResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/shop/address/modify/{addressId}", addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressModifyRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").value(addressResponseDto.getAlias()))
                .andExpect(jsonPath("$.receiver_name").value(addressResponseDto.getReceiverName()))
                .andExpect(jsonPath("$.receiver_phone_number").value(addressResponseDto.getReceiverPhoneNumber()))
                .andExpect(jsonPath("$.zipcode").value(addressResponseDto.getZipcode()))
                .andExpect(jsonPath("$.address").value(addressResponseDto.getAddress()))
                .andExpect(jsonPath("$.address_detail").value(addressResponseDto.getAddressDetail()))
                .andExpect(jsonPath("$.is_default").value(addressResponseDto.getIsDefault()));
    }

    @Test
    @DisplayName(value = "주소 삭제")
    void testDeleteAddress_ReturnsOkStatus() throws Exception {
        Long addressId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/shop/address/delete/{addressId}", addressId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
