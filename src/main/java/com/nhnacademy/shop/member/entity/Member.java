package com.nhnacademy.shop.member.entity;

import com.nhnacademy.shop.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "member")
public class Member implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "member_id", nullable = false)
    @Length(min = 20)
    private String memberId;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "grade_id", nullable = false)
    private Long gradeId;
}
