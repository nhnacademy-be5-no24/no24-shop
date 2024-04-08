package com.nhnacademy.shop.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomerDto {
    private Long customerId;
    private String customerPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private LocalDateTime customerBirthDay;
    private Boolean customerType;
}
