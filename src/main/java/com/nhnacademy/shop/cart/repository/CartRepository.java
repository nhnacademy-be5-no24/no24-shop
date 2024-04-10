package com.nhnacademy.shop.cart.repository;

import com.nhnacademy.shop.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 장바구니 Repository
 *
 * @Author: jinjiwon
 * @Date: 05/04/2024
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMemberCustomerNo(Long customerNo);
}
