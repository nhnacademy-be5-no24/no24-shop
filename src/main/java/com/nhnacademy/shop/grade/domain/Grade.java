package com.nhnacademy.shop.grade.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@Entity
@Table(name = "grade")
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