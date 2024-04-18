package com.nhnacademy.shop.like.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LikeRepositoryCustom {

    Long countBookLikes(String bookIsbn);

}
