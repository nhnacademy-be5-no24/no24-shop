package com.nhnacademy.shop.wrap.domain;

import lombok.*;

import javax.persistence.*;


/**
 * 포장(Wrap) 테이블.
 *
 * @author : 박동희
 * @date : 2024-03-29
 *
 **/
@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wrapId;

    @Column(name = "wrap_name")
    private String wrapName;

    @Column(name = "wrap_cost")
    private Long wrapCost;

}
