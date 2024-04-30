package com.nhnacademy.shop.orders.repository;


import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.orders.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String>, OrdersRepositoryCustom{
    List<Orders> findByCustomer(Customer customer);
}
