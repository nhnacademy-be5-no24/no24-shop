package com.nhnacademy.shop.member.domain;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.customer.domain.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "customer_no")
    private Long customerNo;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_no")
    private Customer customer;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "role")
    private String role;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_leave")
    private Boolean isLeave;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
}
