package com.spring1.meliSocial.integrationTest;


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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring1.meliSocial.dto.request.RequestPostDto;
import com.spring1.meliSocial.dto.response.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    static ObjectMapper mapper;

    private static final ProductDto product1 = new ProductDto(17, "product1", "Ropa", "Marca", "Color", "");
    private static final RequestPostDto post1 = new RequestPostDto(1, LocalDate.now(), product1, 2, 100.0);
    String testDesc;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    private static Stream<Arguments> provideInvalidProducts() {
        return Stream.of(
                Arguments.of(
                        "POST /products/post Invalid Product Id",
                        "El id debe ser mayor a cero",
                        new ProductDto(0, "ValidName", "Ropa", "Marca", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Name Blank",
                        "El campo product_name no puede estar vacío",
                        new ProductDto(10, "", "Ropa", "Marca", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Name too long",
                        "La longitud de product_name no puede superar los 40 caracteres",
                        new ProductDto(10, "a".repeat(41), "Ropa", "Marca", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Name invalid characters",
                        "El campo product_name no puede poseer caracteres especiales",
                        new ProductDto(10, "Invalid@Name", "Ropa", "Marca", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Type too long",
                        "La longitud de type no puede superar los 15 caracteres",
                        new ProductDto(10, "product1", "a".repeat(16), "Marca", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Type invalid characters",
                        "El campo type no puede poseer caracteres especiales",
                        new ProductDto(10, "product1", "Ropa,", "Marca", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Brand too long",
                        "La longitud de brand no puede superar los 25 caracteres",
                        new ProductDto(10, "product1", "Ropa", "a".repeat(26), "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Brand invalid characters",
                        "El campo brand no puede poseer caracteres especiales",
                        new ProductDto(10, "product1", "Ropa", "Marca,", "Color", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Color too long",
                        "La longitud de color no puede superar los 15 caracteres",
                        new ProductDto(10, "product1", "Ropa", "Marca", "a".repeat(16), "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Color invalid characters",
                        "El campo color no puede poseer caracteres especiales",
                        new ProductDto(10, "product1", "Ropa", "Marca", "Color,", "")
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Notes too long",
                        "La longitud de notes no puede superar los 80 caracteres",
                        new ProductDto(10, "product1", "Ropa", "Marca", "Color", "a".repeat(81))
                ),
                Arguments.of(
                        "POST /products/post Invalid Product Notes invalid characters",
                        "El campo notes no puede poseer caracteres especiales",
                        new ProductDto(10, "product1", "Ropa", "Marca", "Color", "a,")
                )
        );
    }

    private static Stream<Arguments> provideInvalidPosts() {
        return Stream.of(
                Arguments.of(
                        "POST /products/post UserId less than zero or null",
                        "El id debe ser mayor a cero",
                        new RequestPostDto(0, LocalDate.now(), null, 2, 100.0)
                ),
                Arguments.of(
                        "POST /products/post Blank Date",
                        "La fecha no puede estar vacía",
                        new RequestPostDto(1, null, null, 2, 100.0)
                ),
                Arguments.of(
                        "POST /products/post Category less than zero or null",
                        "El category debe ser mayor a cero",
                        new RequestPostDto(1, LocalDate.now(), null, 0, 100.0)
                ),
                Arguments.of(
                        "POST /products/post Price less than 0.01 or null",
                        "El precio mínimo por producto es de 0.01",
                        new RequestPostDto(1, LocalDate.now(), null, 2, 0.0)
                ),
                Arguments.of(
                        "POST /products/post Price more than 10000000",
                        "El precio máximo por producto es de 10.000.000",
                        new RequestPostDto(1, LocalDate.now(), null, 2, 10000001)
                )
        );
    }

    @Test
    @DisplayName("POST /products/post OK")
    public void testAddNewPost_Ok() throws Exception {
        ResponseDto responseDTO = new ResponseDto("Publicación creada");
        ResultMatcher bodyEsperado = content().json(mapper.writeValueAsString(responseDTO));
        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(post1)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @ParameterizedTest(name = "{index} - {0}")
    @DisplayName("POST /products/post Invalid Posts Field Validation")
    @MethodSource("provideInvalidPosts")
    public void testAddNewPost_InvalidPostField(String _testDesc, String expectedMessage, RequestPostDto invalidPost) throws Exception {
        testDesc = _testDesc;
        ExceptionDto exceptionDto = new ExceptionDto(expectedMessage);
        ResultMatcher bodyEsperado = content().json(mapper.writeValueAsString(exceptionDto));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        invalidPost.setProduct(product1);

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(invalidPost)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @ParameterizedTest(name = "{index} - {0}")
    @DisplayName("POST /products/post Invalid Product Field Validation")
    @MethodSource("provideInvalidProducts")
    public void testAddNewPost_InvalidProductField(String _testDesc, String expectedMessage, ProductDto invalidProduct) throws Exception {
        testDesc = _testDesc;
        ExceptionDto exceptionDto = new ExceptionDto(expectedMessage);
        ResultMatcher bodyEsperado = content().json(mapper.writeValueAsString(exceptionDto));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        RequestPostDto invalidPost = new RequestPostDto(1, LocalDate.now(), invalidProduct, 2, 100.0);

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(invalidPost)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
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
