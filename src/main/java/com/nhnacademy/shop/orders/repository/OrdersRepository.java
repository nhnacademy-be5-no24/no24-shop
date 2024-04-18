package com.nhnacademy.delivery.orders.repository;

import com.nhnacademy.delivery.orders.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String>, OrdersRepositoryCustom{


}
