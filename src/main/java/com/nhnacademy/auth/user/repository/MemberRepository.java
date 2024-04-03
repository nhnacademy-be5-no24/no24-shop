package com.nhnacademy.auth.user.repository;

import com.nhnacademy.auth.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByCustomerNo(Long customerNo);

}