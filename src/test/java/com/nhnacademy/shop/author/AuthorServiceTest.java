package com.nhnacademy.shop.author;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;
import com.nhnacademy.shop.author.exception.NotFoundAuthorException;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import com.nhnacademy.shop.author.service.AuthorService;
import com.nhnacademy.shop.author.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Transactional
@WebAppConfiguration
class AuthorServiceTest {

    @Mock
    private final AuthorRepository authorRepository = mock(AuthorRepository.class);

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(authorRepository);
    }


    @Test
    void testGetAuthorsByAuthorName() {
        String authorName = "박동희";
        List<Author> authors = new ArrayList<>();
        Author author1 = new Author(1L, "박동희");
        Author author2 = new Author(2L, "박동희");
        authors.add(author1);
        authors.add(author2);

        Page<Author> authorPage = new PageImpl<>(authors);
        when(authorRepository.findAuthorsByAuthorName(authorName, PageRequest.of(0, 10))).thenReturn(authorPage);

        Page<AuthorResponseDto> resultPage = authorService.getAuthorsByAuthorName(authorName, 0, 10);
        List<AuthorResponseDto> result = resultPage.getContent();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getAuthorId());
        assertEquals("박동희", result.get(0).getAuthorName());
        assertEquals(2L, result.get(1).getAuthorId());
        assertEquals("박동희", result.get(1).getAuthorName());

        assertEquals(0, resultPage.getNumber());
        assertEquals(2, resultPage.getSize());
        assertEquals(1, resultPage.getTotalPages());
        assertEquals(2, resultPage.getTotalElements());
    }

    @Test
    void testGetAuthorById(){
        Long authorId = 1L;
        Author author = new Author(authorId, "박동희");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.getAuthorById(authorId);

        assertTrue(result.isPresent());
        assertEquals(authorId, result.get().getAuthorId());
        assertEquals("박동희", result.get().getAuthorName());

        verify(authorRepository).findById(authorId);
    }

    @Test
    void testSaveAuthor(){
        String authorName = "박동희";

        Author author = new Author(1L, authorName);
        when(authorRepository.save(any())).thenReturn(author);

        AuthorRequestDto authorRequestDto = new AuthorRequestDto();
        authorRequestDto.setAuthorName(authorName);
        AuthorResponseDto authorResponseDto = authorService.saveAuthor(authorRequestDto);

        assertNotNull(authorResponseDto);
        assertEquals(author.getAuthorId(), authorResponseDto.getAuthorId());
        assertEquals(author.getAuthorName(), authorResponseDto.getAuthorName());
    }
    @Test
    void testModifyAuthor(){
        Long authorId = 1L;
        ModifyAuthorRequestDto modifyAuthorRequestDto = new ModifyAuthorRequestDto();
        modifyAuthorRequestDto.setAuthorId(authorId);
        modifyAuthorRequestDto.setAuthorName("바꿀 이름");


        Author existingAuthor = new Author(authorId, "원래 이름");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any())).thenReturn(new Author(1L, "바꿀 이름"));
        AuthorResponseDto authorResponseDto = authorService.modifyAuthor(modifyAuthorRequestDto);

        assertNotNull(authorResponseDto);
        assertEquals(1L, authorResponseDto.getAuthorId());
        assertEquals("바꿀 이름", authorResponseDto.getAuthorName());
    }
    @Test
    void testModifyAuthor_WhenNonExistingAuthor() {
        ModifyAuthorRequestDto requestDto = new ModifyAuthorRequestDto();
        requestDto.setAuthorId(1L);
        requestDto.setAuthorName("New Author Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundAuthorException.class, () -> authorService.modifyAuthor(requestDto));
    }

    @Test
    void testDeleteAuthor(){
        Long authorId = 1L;
        String authorName = "박동희";
        Author author = new Author(authorId, authorName);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        authorService.deleteAuthorById(authorId);
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    void testDeleteAuthor_whenNonExistingException(){
        Long nonExistingAuthorId = 1L;

        when(authorRepository.findById(nonExistingAuthorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundAuthorException.class, () -> authorService.deleteAuthorById(nonExistingAuthorId));
        verify(authorRepository, never()).deleteById(nonExistingAuthorId);

    }
}




