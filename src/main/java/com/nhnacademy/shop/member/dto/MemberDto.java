package com.nhnacademy.shop.member.dto;

import com.nhnacademy.shop.customer.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemberDto {
    private CustomerDto customerDto;
    private String memberId;
    private LocalDateTime lastLoginAt;
    private Long gradeId;
    private Integer memberStateId;
    private String role;
}
