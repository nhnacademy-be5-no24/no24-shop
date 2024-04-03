package com.nhnacademy.auth.user.repository;

import com.nhnacademy.auth.user.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Long> {
    Grade deleteByGradeId(Long id);
}