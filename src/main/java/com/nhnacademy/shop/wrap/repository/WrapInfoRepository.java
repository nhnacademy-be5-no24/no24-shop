package com.nhnacademy.shop.wrap.repository;

import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 포장(Wrap) 세부 정보 repository.
 *
 * @author : 박동희
 * @date : 2024-03-29
 *
 **/
public interface WrapInfoRepository extends JpaRepository<WrapInfo, WrapInfo.Pk> {

}
