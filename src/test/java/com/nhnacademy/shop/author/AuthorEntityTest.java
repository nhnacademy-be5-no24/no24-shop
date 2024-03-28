package com.nhnacademy.shop.author;

import com.nhnacademy.shop.author.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class AuthorEntityTest {

    @Test
    void testAuthorEntityGettersAndSetters() {

        Author author = new Author(1L, "parkdonghui");

        Long authorId = author.getAuthorId();
        String authorName = author.getAuthorName();

        assertEquals(Long.valueOf(1L), authorId);
        assertEquals("parkdonghui", authorName);
    }
}
