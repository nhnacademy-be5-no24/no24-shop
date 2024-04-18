package com.nhnacademy.delivery.order_detail.repository;

import com.nhnacademy.delivery.order_detail.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

}
