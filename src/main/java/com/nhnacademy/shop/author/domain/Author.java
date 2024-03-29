package com.nhnacademy.shop.author.domain;


import lombok.*;

import javax.persistence.*;

/**
 * @Author : 박동희
 * @Date : 20/03/2024
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
