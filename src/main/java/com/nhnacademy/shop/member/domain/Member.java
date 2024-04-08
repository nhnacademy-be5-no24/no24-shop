package com.nhnacademy.shop.member.domain;

import com.nhnacademy.shop.customer.entity.Customer;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "member")
public class Member implements Serializable {
    @Id
    @Column(name = "customer_no")
    private Long customerNo;

    @Column(name = "member_id", nullable = false)
    @Length(max = 20)
    private String memberId;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "grade_id", nullable = false)
    private Long gradeId;
}
