package com.nhnacademy.shop.category.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * 카테고리(Category) 테이블.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "category_name")
    @Length(min = 1, max = 20)
    private String categoryName;

    @JoinColumn(name = "parent_category_id", referencedColumnName = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;

}
