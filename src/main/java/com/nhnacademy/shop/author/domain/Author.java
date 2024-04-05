package com.nhnacademy.shop.author.domain;


import lombok.*;

import javax.persistence.*;

/**
 * 저자(Author) 테이블.
 *
 * @Author : 박동희
 * @Date : 2024-03-20
 */
@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(name = "author_name")
    private String authorName;
}
