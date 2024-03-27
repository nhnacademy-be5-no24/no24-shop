package com.nhnacademy.shop.author;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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

        List<Author> authors = authorRepository.findAuthorsByAuthorName("박동");
        assertEquals(1, authors.size());
    }
}

