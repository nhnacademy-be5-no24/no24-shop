package com.nhnacademy.shop.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.author.controller.AuthorController;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;
import com.nhnacademy.shop.author.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthorController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    ObjectMapper objectMapper = new ObjectMapper();
    AuthorRequestDto authorRequestDto;
    ModifyAuthorRequestDto modifyAuthorRequestDto;
    AuthorResponseDto authorResponseDto;


    @BeforeEach
    void setUp() {
        authorRequestDto = new AuthorRequestDto();
        authorRequestDto.setAuthorName("test authorName");
        authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setAuthorId(1L);
        authorResponseDto.setAuthorName("test authorName");
    }

    @Test
    void testGetAuthorsByAuthorName() throws Exception {

        when(authorService.getAuthorsByAuthorName("Test")).thenReturn(Collections.singletonList(new AuthorResponseDto()));
        mockMvc.perform(MockMvcRequestBuilders.get("/shop/authors/Test"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }
    @Test
    void testGetAuthorsByNonExistentAuthorName() throws Exception {
        when(authorService.getAuthorsByAuthorName("NonExistent")).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/shop/authors/NonExistent"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testSaveAuthor() throws Exception {
        when(authorService.saveAuthor(any(AuthorRequestDto.class))).thenReturn(authorResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/shop/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("test authorName"));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("test authorName"));
    }

    @Test
    void testDeleteAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shop/authors/1"))
                .andExpect(status().isNoContent());
    }


}
