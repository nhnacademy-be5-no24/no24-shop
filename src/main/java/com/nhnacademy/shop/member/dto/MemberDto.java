package com.nhnacademy.shop.member.dto;

import com.nhnacademy.shop.customer.dto.CustomerDto;
import com.nhnacademy.shop.member.domain.Member;
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
    private Member.MemberState memberStateId;
    private String role;
}
