package com.nhnacademy.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberCreateDto {
    private String customerId;
    private String customerPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private LocalDate customerBirthday;

    private Long gradeId;
}