package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.ExceptionDto;
import com.spring1.meliSocial.dto.response.PostIndexDto;
import com.spring1.meliSocial.dto.response.ProductDto;
import com.spring1.meliSocial.dto.response.ResponsePostDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    static ObjectMapper mapper;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("Obtener posts de un usuario que no existe")
    public void testGetPostsByUser_UserNotExists() throws Exception {
        int userId = 300;
        String messageExpected = "No se encontró el usuario.";

        ResultMatcher expectedStatus = status().isNotFound();
        ResultMatcher expectedContentType = content().contentType("application/json");
        ResultMatcher expectedBody = content().json(mapper.writeValueAsString(new ExceptionDto(messageExpected)));

        this.mockMvc
                .perform(get("/products/followed/{userId}/list", userId))
                .andExpectAll(
                        expectedStatus, expectedContentType, expectedBody
        ).andDo(print());
    }

    @Test
    @DisplayName("Obtener posts de un usuario que existe pero no sigue a nadie")
    public void testGetPostsByUser_UserWithZeroFollowed() throws Exception {
        int userId = 1;
        PostIndexDto expectedJsonContent = new PostIndexDto(userId, new ArrayList<>());

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType("application/json");
        ResultMatcher expectedBody = content().json(mapper.writeValueAsString(expectedJsonContent));

        this.mockMvc
                .perform(get("/products/followed/{userId}/list", userId))
                .andExpectAll(
                        expectedStatus, expectedContentType, expectedBody
                ).andDo(print());
    }

    @Test
    @DisplayName("Obtener posts de un usuario con seguidos de manera ascendente")
    public void testGetPostsByUser_UserWithFollowedByAscOrder() throws Exception {
        int userId = 9;

        ResponsePostDto post1 = new ResponsePostDto(
                18,
                8,
                LocalDate.parse("02-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                new ProductDto(15,
                        "Mouse",
                        "Tecnología",
                        "Logitech",
                        "Negro",
                        "Mouse hergonómico altamente recomendado!."),
                55,
                15.99,
                true,
                0.5);

        ResponsePostDto post2 = new ResponsePostDto(
                17,
                8,
                LocalDate.parse("01-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                new ProductDto(16,
                        "Gorro",
                        "Ropa",
                        "DC",
                        "Negro",
                        "Gorro negro skater"),
                2,
                20.99,
                true,
                0.3);

        PostIndexDto expectedJsonContent = new PostIndexDto(userId, new ArrayList<>(List.of(post1, post2)));

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType("application/json");
        ResultMatcher expectedBody = content().json(mapper.writeValueAsString(expectedJsonContent));

        this.mockMvc
                .perform(get("/products/followed/{userId}/list", userId)
                        .param("order", "date_asc"))
                .andExpectAll(
                        expectedStatus, expectedContentType, expectedBody
                ).andDo(print());
    }

    @Test
    @DisplayName("Obtener posts de un usuario con seguidos de manera descendente")
    public void testGetPostsByUser_UserWithFollowedByDescOrder() throws Exception {
        int userId = 9;

        ResponsePostDto post1 = new ResponsePostDto(
                18,
                8,
                LocalDate.parse("02-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                new ProductDto(15,
                        "Mouse",
                        "Tecnología",
                        "Logitech",
                        "Negro",
                        "Mouse hergonómico altamente recomendado!."),
                55,
                15.99,
                true,
                0.5);

        ResponsePostDto post2 = new ResponsePostDto(
                17,
                8,
                LocalDate.parse("01-01-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                new ProductDto(16,
                        "Gorro",
                        "Ropa",
                        "DC",
                        "Negro",
                        "Gorro negro skater"),
                2,
                20.99,
                true,
                0.3);

        PostIndexDto expectedJsonContent = new PostIndexDto(userId, new ArrayList<>(List.of(post2, post1)));

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType("application/json");
        ResultMatcher expectedBody = content().json(mapper.writeValueAsString(expectedJsonContent));

        this.mockMvc
                .perform(get("/products/followed/{userId}/list", userId)
                        .param("order", "date_desc"))
                .andExpectAll(
                        expectedStatus, expectedContentType, expectedBody
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Query param 'order' inválido")
    public void  testGetPostsByUser_InvalidQueryParam() throws Exception {
        int userId = 9;
        String messageExpected = "Parámetros inválidos.";

        ResultMatcher expectedStatus = status().isBadRequest();
        ResultMatcher expectedContentType = content().contentType("application/json");
        ResultMatcher expectedBody = content().json(mapper.writeValueAsString(new ExceptionDto(messageExpected)));

        this.mockMvc
                .perform(get("/products/followed/{userId}/list", userId)
                        .param("order", "date_desccc"))
                .andExpectAll(
                        expectedStatus, expectedContentType, expectedBody
                )
                .andDo(print());
    }
}
