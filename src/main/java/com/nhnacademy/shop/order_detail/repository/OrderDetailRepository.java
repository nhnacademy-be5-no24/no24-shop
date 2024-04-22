package com.nhnacademy.shop.order_detail.repository;

import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

}
