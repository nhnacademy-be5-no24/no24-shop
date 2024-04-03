package com.nhnacademy.auth.user.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;
    @Column(name = "grade_name")
    private String gradeName;
    @Column(name = "accumulate_rate")
    private Long accumulateRate;
}