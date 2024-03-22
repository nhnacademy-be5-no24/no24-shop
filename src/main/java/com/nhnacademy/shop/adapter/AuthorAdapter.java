package com.nhnacademy.shop.adapter;

import com.nhnacademy.shop.author.domain.Author;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "shop-service", path="/shop/author")
public interface AuthorAdapter {
    @GetMapping("/{id}")
    Optional<Author> getAuthor(@PathVariable("id") Long id);
}
