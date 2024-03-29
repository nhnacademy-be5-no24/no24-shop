package com.nhnacademy.shop.customer.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_no")
    private Long customerNo;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_password")
    private String customerPassword;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone_numeber")
    private String customerPhoneNumber;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_birthday")
    private LocalDate customerBirthday;

    @Column(name = "customer_role", nullable = false)
    private String customerRole;
}
