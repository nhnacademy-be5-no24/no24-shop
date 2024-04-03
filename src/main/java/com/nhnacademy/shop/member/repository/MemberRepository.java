package com.nhnacademy.shop.member.repository;

import com.nhnacademy.shop.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByCustomerNo(Long customerNo);

}
