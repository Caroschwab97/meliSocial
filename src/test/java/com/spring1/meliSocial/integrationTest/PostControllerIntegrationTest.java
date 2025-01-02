package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring1.meliSocial.dto.ExceptionDto;
import com.spring1.meliSocial.dto.request.ProductPromoDto;
import com.spring1.meliSocial.dto.request.RequestPostDto;
import com.spring1.meliSocial.dto.response.PostPromoDto;
import com.spring1.meliSocial.dto.response.ProductDto;
import com.spring1.meliSocial.dto.response.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private static final ProductDto product1 = new ProductDto(17, "product1", "Ropa", "Marca", "Color", "");
    private static final RequestPostDto post1 = new RequestPostDto(1, LocalDate.now(), product1, 2, 100.0);
    private static final ProductPromoDto postPromo1 = new ProductPromoDto(1, LocalDate.now(), product1, 2, 100.0, true, 0.10);

    ObjectMapper objectMapper = new ObjectMapper();
    String testDesc;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
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

    private static Stream<Arguments> provideInvalidPromoPosts() {
        return Stream.of(
                Arguments.of(
                        "POST /products/promo-post UserId less than zero or null",
                        "El id debe ser mayor a cero",
                        new ProductPromoDto(0, LocalDate.now(), null, 2, 100.0, true, 0.10)
                ),
                Arguments.of(
                        "POST /products/promo-post Blank Date",
                        "La fecha no puede estar vacía",
                        new ProductPromoDto(1, null, null, 2, 100.0, true, 0.10)
                ),
                Arguments.of(
                        "POST /products/promo-post Category less than zero or null",
                        "El category debe ser mayor a cero",
                        new ProductPromoDto(1, LocalDate.now(), null, 0, 100.0, true, 0.10)
                ),
                Arguments.of(
                        "POST /products/promo-post Price less than 0.01 or null",
                        "El precio mínimo por producto es de 0.01",
                        new ProductPromoDto(1, LocalDate.now(), null, 2, 0.0, true, 0.10)
                ),
                Arguments.of(
                        "POST /products/promo-post Price more than 10000000",
                        "El precio máximo por producto es de 10.000.000",
                        new ProductPromoDto(1, LocalDate.now(), null, 2, 10000001, true, 0.10)
                ),
                Arguments.of(
                        "POST /products/promo-post HasPromo false",
                        "El valor del campo has_promo debe ser verdadero para admitir la promo",
                        new ProductPromoDto(1, LocalDate.now(), null, 2, 100.0, false, 0.10)
                ),
                Arguments.of(
                        "POST /products/promo-post Discount more than 100%",
                        "El descuento máximo no puede superar el 100%",
                        new ProductPromoDto(1, LocalDate.now(), null, 2, 100.0, true, 1.5)
                ),
                Arguments.of(
                        "POST /products/promo-post Discount is 0%",
                        "El descuento mínimo no puede ser cero",
                        new ProductPromoDto(1, LocalDate.now(), null, 2, 100.0, true, 0)
                )
        );
    }

    @Test
    @DisplayName("POST /products/post OK")
    public void testAddNewPost_Ok() throws Exception {
        ResponseDto responseDTO = new ResponseDto("Publicación creada");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(responseDTO));
        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(post1)))
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
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDto));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        invalidPost.setProduct(product1);

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidPost)))
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
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDto));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        RequestPostDto invalidPost = new RequestPostDto(1, LocalDate.now(), invalidProduct, 2, 100.0);

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/post Product already exists")
    public void testAddNewPost_Product_Conflict() throws Exception {
        int invalidProductId = 1;
        ExceptionDto exceptionDTO = new ExceptionDto("El producto con el id " + invalidProductId + " ya existe");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isConflict();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        ProductDto productPromo = new ProductDto(invalidProductId, "product1", "Ropa", "Marca", "Color", "");
        RequestPostDto invalidPost = new RequestPostDto(1, LocalDate.now(), productPromo, 2, 100.0);

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/post User Not Found")
    public void testAddNewPost_User_Not_Found() throws Exception {
        int invalidUserId = 120;
        ExceptionDto exceptionDTO = new ExceptionDto("El usuario con id: " + invalidUserId + " no existe");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isNotFound();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        RequestPostDto postUserNotFound = new RequestPostDto(invalidUserId, LocalDate.now(), product1, 2, 100.0);

        mockMvc.perform(post("/products/post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postUserNotFound)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/promo-post OK")
    public void testPost_Ok() throws Exception {
        ResponseDto responseDTO = new ResponseDto("Publicación con promoción creada");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(responseDTO));
        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postPromo1)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @ParameterizedTest(name = "{index} - {0}")
    @DisplayName("POST /products/promo-post Invalid Posts Field Validation")
    @MethodSource("provideInvalidPromoPosts")
    public void testPost_InvalidPostField(String _testDesc, String expectedMessage, ProductPromoDto invalidPost) throws Exception {
        testDesc = _testDesc;
        ExceptionDto exceptionDto = new ExceptionDto(expectedMessage);
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDto));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        invalidPost.setProduct(product1);

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @ParameterizedTest(name = "{index} - {0}")
    @DisplayName("POST /products/promo-post Invalid Product Field Validation")
    @MethodSource("provideInvalidProducts")
    public void testPost_InvalidProductField(String _testDesc, String expectedMessage, ProductDto invalidProduct) throws Exception {
        testDesc = _testDesc;
        ExceptionDto exceptionDto = new ExceptionDto(expectedMessage);
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDto));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        ProductPromoDto invalidPost = new ProductPromoDto(1, LocalDate.now(), invalidProduct, 2, 100.0, true, 0.10);

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/promo-post User Not Found")
    public void testPost_User_Not_Found() throws Exception {
        int invalidUserId = 120;
        ExceptionDto exceptionDTO = new ExceptionDto("El id del vendedor no existe");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isNotFound();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        ProductPromoDto postPromoUserNotFound = new ProductPromoDto(invalidUserId, LocalDate.now(), product1, 2, 100.0, true, 0.10);

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postPromoUserNotFound)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/promo-post Product already exists")
    public void testPost_Product_Conflict() throws Exception {
        int invalidProductId = 1;
        ExceptionDto exceptionDTO = new ExceptionDto("El id del producto ya existe en una publicación");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        ProductDto productPromo = new ProductDto(invalidProductId, "product1", "Ropa", "Marca", "Color", "");
        ProductPromoDto postPromoUserIsNotSeller = new ProductPromoDto(1, LocalDate.now(), productPromo, 2, 100.0, true, 0.10);

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postPromoUserIsNotSeller)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/promo-post User Is Not Seller")
    public void testPost_User_Is_Not_Seller() throws Exception {
        int invalidUserId = 2;
        ExceptionDto exceptionDTO = new ExceptionDto("El id que ingresó es de un usuario comprador");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        ProductPromoDto postPromoUserIsNotSeller = new ProductPromoDto(invalidUserId, LocalDate.now(), product1, 2, 100.0, true, 0.10);

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postPromoUserIsNotSeller)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/promo-post Invalid Json")
    public void testPost_testCreateInvalidJson() throws Exception {
        ExceptionDto exceptionDTO = new ExceptionDto("Formato invalido en la request.");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content("\"dsa\""))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("POST /products/promo-post Invalid Date Format")
    public void testPost_testCreateInvalidDateFormat() throws Exception {
        ExceptionDto exceptionDTO = new ExceptionDto("Ingrese un formato válido de fecha como dd-MM-YYYY");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(exceptionDTO));
        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(post("/products/promo-post")
                        .contentType("application/json")
                        .content("{\"userId\":1,\"date\":\"02-01\",\"product\":{\"type\":\"Ropa\",\"brand\":\"Marca\",\"color\":\"Color\",\"notes\":\"\",\"id\":17,\"name\":\"product1\"},\"category\":2,\"price\":100.0,\"hasPromo\":true,\"discount\":0.1}"))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }


    @Test
    @DisplayName("Obtener cantidad de productos en promo de un vendedor")
    public void testGetPromoPostCount() throws Exception {
        int userId = 1;
        PostPromoDto esperado = new PostPromoDto(
                userId,
                "Agustina Lopez",
                11);

        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(esperado));

        mockMvc.perform(get("/products/promo-post/count")
                        .param("user_id", String.valueOf(userId)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andExpect(bodyEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener NotFound de la cantidad de productos en promo de un vendedor que no existe")
    public void testGetPromoPostCount_WithInvalidUser() throws Exception {
        int userId = 99;

        ResultMatcher statusEsperado = status().isNotFound();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(get("/products/promo-post/count")
                        .param("user_id", String.valueOf(userId)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener BadRequest de la cantidad de productos en promo de un usuario que no es vendedor")
    public void testGetPromoPostCount_WithNonSellerUser() throws Exception {
        int userId = 2;

        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(get("/products/promo-post/count")
                        .param("user_id", String.valueOf(userId)))
                .andExpect(statusEsperado)
                .andExpect(contentTypeEsperado)
                .andDo(print());
    }
}
