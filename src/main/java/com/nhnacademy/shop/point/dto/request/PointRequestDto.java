package com.nhnacademy.shop.point.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.shop.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 포인트 내역 생성을 위한 Dto 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRequestDto {
    @NotNull(message = "회원 번호를 입력해주세요.")
    private Long customerNo;

    @NotNull(message = "주문 아아디를 입력해주세요.")
    private String orderId;

    @NotNull
    @NotBlank(message = "포인트 사용 내역을 입력해주세요.")
    @Size(max = 50, message = "포인트 사용 내역은 50자 까지 입력하실 수 있습니다.")
    private String pointDescription;

    @NotNull(message = "사용 및 적립 금액을 입력해주세요.")
    private Integer usage;

    @NotNull(message = "사용 내역 날짜를 입력해주세요.")
    private LocalDateTime createdAt;
}
