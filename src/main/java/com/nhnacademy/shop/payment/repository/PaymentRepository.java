package com.nhnacademy.shop.payment.repository;

import com.nhnacademy.shop.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 결제 수단(Payment) repository.
 *
 * @author : 박동희
 * @date : 2024-04-01
 *
 **/
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
