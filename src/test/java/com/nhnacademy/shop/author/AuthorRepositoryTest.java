package com.nhnacademy.shop.author;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testFindAuthorsByAuthorName() {
        Author author1 = new Author(null, "박동");
        Author author2 = new Author(null, "박희");
        Author saved_author1 = authorRepository.save(author1);
        Author saved_author2 = authorRepository.save(author2);

        Page<Author> authorsPage = authorRepository.findAuthorsByAuthorName("박동", PageRequest.of(0, 10));
        List<Author> authors = authorsPage.getContent();
        assertEquals(1, authors.size());
    }
}

