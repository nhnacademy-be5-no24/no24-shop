package com.nhnacademy.shop.author;

import com.nhnacademy.shop.author.domain.Author;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorEntityTest {

    @Test
    void testAuthorEntityGettersAndSetters() {

        Author author = new Author();
        author.setAuthorId(1L);
        author.setAuthorName("parkdonghui");

        Long authorId = author.getAuthorId();
        String authorName = author.getAuthorName();

        assertEquals(Long.valueOf(1L), authorId);
        assertEquals("parkdonghui", authorName);
    }
}
