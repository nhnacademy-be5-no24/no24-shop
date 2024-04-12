package com.nhnacademy.shop.category.repository.impl;


import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.category.domain.QCategory;
import com.nhnacademy.shop.category.dto.response.ChildCategoryResponseDto;
import com.nhnacademy.shop.category.dto.response.CategoryInfoResponseDto;
import com.nhnacademy.shop.category.dto.response.ParentCategoryResponseDto;
import com.nhnacademy.shop.category.repository.CategoryRepositoryCustom;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
/**
 * 카테고리 Custom Repository 구현체 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 **/
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CategoryRepositoryCustom{
    public CategoryRepositoryImpl() {
        super(Category.class);
    }

    QCategory parentCategory = new QCategory("parentCategory");
    /**
     * {@inheritDoc}
     */
    @Override
    public List<CategoryInfoResponseDto> getCategoriesInfo() {
        return from(parentCategory)
                .select(Projections.constructor(CategoryInfoResponseDto.class,
                        parentCategory.categoryId,
                        parentCategory.categoryName))
                .orderBy(parentCategory.categoryName.asc())
                .distinct()
                .fetch();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParentCategoryResponseDto> getParentCategoriesWithChildCategories() {
        List<ParentCategoryResponseDto> parentList = from(parentCategory)
                .where(parentCategory.parentCategory.isNull())
                .select(Projections.constructor(ParentCategoryResponseDto.class,
                        parentCategory.categoryId,
                        parentCategory.categoryName))
                .orderBy(parentCategory.categoryName.asc())
                .fetch();

        QCategory childCategory = QCategory.category;

        parentList.forEach(p -> {
            List<ChildCategoryResponseDto> childList = from(childCategory)
                    .select(Projections.constructor(ChildCategoryResponseDto.class,
                            childCategory.categoryId,
                            childCategory.categoryName))
                    .where(childCategory.parentCategory.categoryId.eq(p.getCategoryId()))
                    .orderBy(childCategory.categoryId.asc(), childCategory.categoryName.asc())
                    .fetch();
            p.setChildCategories(childList);
        });
        return parentList;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ParentCategoryResponseDto getParentCategory(Long categoryId) {
        ParentCategoryResponseDto parentDto = from(parentCategory)
                .where(parentCategory.parentCategory.isNull().and(parentCategory.categoryId.eq(categoryId)))
                .select(Projections.constructor(ParentCategoryResponseDto.class,
                        parentCategory.categoryId,
                        parentCategory.categoryName))
                .orderBy(parentCategory.categoryName.asc())
                .fetchOne();

        QCategory childCategory = QCategory.category;

        parentDto.setChildCategories(from(childCategory)
                .select(Projections.constructor(ChildCategoryResponseDto.class,
                        childCategory.categoryId,
                        childCategory.categoryName))
                .where(childCategory.parentCategory.categoryId.eq(parentDto.getCategoryId()))
                .orderBy(childCategory.categoryId.asc(), childCategory.categoryName.asc())
                .fetch());
        return parentDto;
    }

}