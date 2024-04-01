package com.nhnacademy.shop.address.repository;

import com.nhnacademy.shop.address.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerNo(Long customerNo);
}
