package com.nhnacademy.auth.user.repository;

import com.nhnacademy.auth.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByCustomerId(String customerId);
}