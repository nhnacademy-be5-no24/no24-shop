package com.nhnacademy.shop.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "customer_password")
    @Length(max = 20)
    private String customerPassword;

    @Column(name = "customer_name")
    @Length(max = 50)
    private String customerName;

    @Column(name = "customer_phone_number")
    @Length(max = 15)
    private String customerPhoneNumber;

    @Column(name = "customer_email")
    @Length(max = 50)
    private String customerEmail;

    @Column(name = "customer_birthday")
    private LocalDate customerBirthday;

    @Column(name = "customer_type", nullable = false)
    private Boolean customerType;

}
