package com.nhnacademy.shop.customer.repository;

import com.nhnacademy.shop.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByCustomerNo(Long customerNo);
}
