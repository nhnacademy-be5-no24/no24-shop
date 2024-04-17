package com.nhnacademy.shop.customer.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    @Id
    @Column(name = "customer_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerNo;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "customer_password")
    private String customerPassword;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;
    @Column(name = "customer_email")
    private String customerEmail;
    @Column(name = "customer_birthday")
    private LocalDate customerBirthday;
    @Column(name = "customer_role")
    private String  customerRole;
}
