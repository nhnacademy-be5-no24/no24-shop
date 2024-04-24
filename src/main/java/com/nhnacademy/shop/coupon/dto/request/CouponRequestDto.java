package com.nhnacademy.shop.coupon.dto.request;

import com.nhnacademy.shop.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 쿠폰 등록을 위한 dto 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequestDto {
    @NotNull
    @NotBlank(message = "쿠폰 이름을 입력해주세요.")
    @Length(max = 30, message = "쿠폰 이름은 30자까지 입력이 가능합니다.")
    private String couponName;
    @NotNull(message = "쿠폰 만료일을 입력해주세요.")
    private Date deadline;
    @NotNull(message = "쿠폰 발급 제한 수량을 입력해주세요.")
    private int issueLimit;
    @NotNull(message = "쿠폰 상태를 입력해주세요.")
    private Coupon.Status couponStatus;
    @NotNull(message = "쿠폰 타입을 입력해주세요.")
    private Coupon.CouponType couponType;
    @NotNull(message = "쿠폰의 타겟 타입을 입력해주세요.")
    private Coupon.CouponTarget couponTarget;

    // for target
    private String bookIsbn;
    private Long categoryId;

    // for coupon type
    private Long discountPrice;
    private Long discountRate;
    private Long maxDiscountPrice;
}
