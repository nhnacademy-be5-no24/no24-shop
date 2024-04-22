package com.nhnacademy.shop.grade.repository;

import com.nhnacademy.shop.grade.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRespository extends JpaRepository<Grade, Long> {
}
