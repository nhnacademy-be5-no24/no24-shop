package com.nhnacademy.shop.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CustomerDto {
    private String customerId;
    private String customerPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private LocalDate customerBirthDay;
    private String customerRole;
}
