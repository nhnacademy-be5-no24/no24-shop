package com.nhnacademy.shop.publisher.repository;

import com.nhnacademy.shop.publisher.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Publisher Repository
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findPublisherByPublisherName(String publisherName);
}
