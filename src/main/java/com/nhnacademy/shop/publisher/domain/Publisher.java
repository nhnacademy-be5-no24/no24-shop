package com.nhnacademy.shop.publisher.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Publisher Domain
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;

    @Column(name = "publisher_name", unique = true)
    private String publisherName;
}
