package com.nhnacademy.shop.wrap.repository;

import com.nhnacademy.shop.wrap.domain.Wrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * 포장(Wrap) repository.
 *
 * @author : 박동희
 * @date : 2024-03-29
 *
 **/
public interface WrapRepository extends JpaRepository<Wrap, Long> {
    /**
     * 포장지이름으로 포장지 조회하는 method.
     *
     * @return 해당 Wrap 반환.
     */
    Optional<Wrap> findWrapByWrapName(String wrapName);
}
