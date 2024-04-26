package com.nhnacademy.shop.member.domain;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    public enum MemberState {
        ACTIVE,INACTIVE,LEAVE,BAN
    }

    @Id
    @Column(name = "customer_no")
    private Long customerNo;

    @OneToOne(cascade = CascadeType.MERGE)
    @MapsId
    @JoinColumn(name = "customer_no")
    private Customer customer;

    @Column(name = "member_id")
    private String memberId;
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    @OneToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;       //class 이름 사용안됨
    @Column(name = "role")
    private String role;

    @Column(name = "member_state")
    private MemberState memberState;
}
