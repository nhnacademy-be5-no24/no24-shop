package com.nhnacademy.shop.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.author.controller.AuthorController;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;
import com.nhnacademy.shop.author.exception.NotFoundAuthorException;
import com.nhnacademy.shop.author.service.AuthorService;
import com.nhnacademy.shop.category.controller.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthorController.class)
class AuthorControllerTest {
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    ObjectMapper objectMapper = new ObjectMapper();
    AuthorRequestDto authorRequestDto;
    ModifyAuthorRequestDto modifyAuthorRequestDto;
    AuthorResponseDto authorResponseDto;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthorController(authorService)).build();
        authorRequestDto = new AuthorRequestDto();
        authorRequestDto.setAuthorName("test authorName");
        authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setAuthorId(1L);
        authorResponseDto.setAuthorName("test authorName");
    }

    @Test
    void testGetAuthorsByAuthorName() throws Exception {
        List<AuthorResponseDto> authors = Collections.singletonList(new AuthorResponseDto());
        when(authorService.getAuthorsByAuthorName("Test", 0, 10))
                .thenReturn(new PageImpl<>(authors, PageRequest.of(0, 10), authors.size()));

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/authors/Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
    @Test
    void testGetAuthorsByNonExistentAuthorName() throws Exception {
        String authorName = "NonExistentAuthor";
        int page = 0;
        int size = 10;
        when(authorService.getAuthorsByAuthorName(authorName, page, size))
                .thenThrow(new NotFoundAuthorException());

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/{authorName}", authorName)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void testSaveAuthor() throws Exception {
        when(authorService.saveAuthor(any(AuthorRequestDto.class))).thenReturn(authorResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.authorName").value("test authorName"));
    }

    @Test
    void testModifyAuthor() throws Exception {
        modifyAuthorRequestDto = new ModifyAuthorRequestDto();
        modifyAuthorRequestDto.setAuthorId(1L);
        modifyAuthorRequestDto.setAuthorName("modified test authorName");

        when(authorService.modifyAuthor(any(ModifyAuthorRequestDto.class))).thenReturn(authorResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/shop/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyAuthorRequestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.authorName").value("test authorName"));
    }

    @Test
    void testDeleteAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shop/authors/1"))
                .andExpect(status().isNoContent());
    }


}
