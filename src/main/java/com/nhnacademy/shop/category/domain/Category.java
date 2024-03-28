package com.nhnacademy.shop.category.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

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


    // TODO #5 Fetch Type LAZY or EAGER ??
    @JoinColumn(name = "parent_category_id", referencedColumnName = "category_id")
    @ManyToOne
    private Category parentCategory;

}
