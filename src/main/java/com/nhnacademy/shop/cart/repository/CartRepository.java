package com.nhnacademy.shop.cart.repository;

import com.nhnacademy.shop.cart.domain.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 장바구니 Repository
 *
 * @Author: jinjiwon
 * @Date: 05/04/2024
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findByMemberCustomerNo(Long customerNo, Pageable pageable);
}
