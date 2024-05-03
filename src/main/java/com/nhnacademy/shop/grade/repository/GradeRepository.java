package com.nhnacademy.shop.grade.repository;

import com.nhnacademy.shop.grade.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
