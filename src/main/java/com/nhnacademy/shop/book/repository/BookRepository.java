package com.nhnacademy.shop.book.repository;

import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.tag.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 도서관리 repository
 *
 * @author : 이재원
 * @date : 2024-03-27
 */
public interface BookRepository extends JpaRepository<Book, String>, BookRepositoryCustom{

    Book findByBookIsbn(String bookIsbn);

    Page<Book> findByCategoriesContaining(Pageable pageable, Category category);

    Page<Book> findByTagsContaining(Pageable pageable, Tag tag);

}
