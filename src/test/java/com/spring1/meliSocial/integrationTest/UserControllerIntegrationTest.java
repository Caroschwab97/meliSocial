package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.response.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "/users/";

    private FollowedDto followed1;
    private FollowedDto followed2;
    private FollowerDto follower1;
    private FollowerDto follower2;
    private FollowerDto follower3;
    private FollowerDto follower4;

    @Test
    @DisplayName("Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor")
    public void testGetFollowerCountOK() throws Exception {
        int parametroEntrada = 1;
        UserFollowersDto result = new UserFollowersDto(1, "Agustina Lopez", 4);

        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(result));

        mockMvc.perform(get("/users/{userId}/followers/count", parametroEntrada))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado, bodyEsperado
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener NotFound de la cantidad de usuarios que siguen a un determinado vendedor")
    public void testGetFollowerCountNotFound() throws Exception {
        int parametroEntrada = 10;
        ResponseDto result = new ResponseDto("El id que busca no existe");
        ResultMatcher statusEsperado = status().isNotFound();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(result));

        mockMvc.perform(get("/users/{userId}/followers/count", parametroEntrada))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado, bodyEsperado
                )
                .andDo(print());
    }


    @Test
    public void testFollowedList() throws Exception {
        followed1 = new FollowedDto(1, "Agustina Lopez");
        followed2 = new FollowedDto(6, "Fausto Smith");

        List<FollowedDto> seguidos = new ArrayList<>(List.of(followed1));
        FollowedByUserDto esperado = new FollowedByUserDto(3, "Carlos Perez", seguidos);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEsperado = objectMapper.writeValueAsString(esperado);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(jsonEsperado));
    }

    @Test
    public void testFollowedListNotFoundID() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list", 100))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testFollowedListAsc() throws Exception {
        followed1 = new FollowedDto(1, "Agustina Lopez");
        followed2 = new FollowedDto(6, "Fausto Smith");

        List<FollowedDto> seguidos = new ArrayList<>(List.of(followed1, followed2));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEsperado = objectMapper.writeValueAsString(seguidos);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.followed", Matchers.hasSize(seguidos.size())))
                .andExpect(jsonPath("$.followed[0].user_id").value(seguidos.get(0).getId()))
                .andExpect(jsonPath("$.followed[0].user_name").value(seguidos.get(0).getUserName()))
                .andExpect(jsonPath("$.followed[1].user_id").value(seguidos.get(1).getId()))
                .andExpect(jsonPath("$.followed[1].user_name").value(seguidos.get(1).getUserName()));
    }

    @Test
    public void testFollowedListDesc() throws Exception {
        followed1 = new FollowedDto(1, "Agustina Lopez");
        followed2 = new FollowedDto(6, "Fausto Smith");

        List<FollowedDto> seguidos = new ArrayList<>(List.of(followed2, followed1));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEsperado = objectMapper.writeValueAsString(seguidos);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list", 5).param("order", "name_desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.followed", Matchers.hasSize(seguidos.size())))
                .andExpect(jsonPath("$.followed[0].user_id").value(seguidos.get(0).getId()))
                .andExpect(jsonPath("$.followed[0].user_name").value(seguidos.get(0).getUserName()))
                .andExpect(jsonPath("$.followed[1].user_id").value(seguidos.get(1).getId()))
                .andExpect(jsonPath("$.followed[1].user_name").value(seguidos.get(1).getUserName()));
    }

    @Test
    @DisplayName("Obtener listado de seguidores de un usuario con el ordenamiento por default (asc)")
    void testGetFollowersFromSeller_WithDefaultOrder() throws Exception {
        int userId = 6;
        follower1 = new FollowerDto(5, "Esteban Marquez");

        List<FollowerDto> followers = new ArrayList<>(List.of(follower1));
        SellerFollowersDto followersEsperado = new SellerFollowersDto(userId, "Fausto Smith", followers);

        String jsonEsperado = objectMapper.writeValueAsString(followersEsperado);

        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(jsonEsperado);

        mockMvc.perform(get(USERS_PATH + "{userId}/followers/list", userId)
                        .param("order", "name_asc"))
                .andExpect(jsonPath("$.followers", Matchers.hasSize(1)))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado, bodyEsperado
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener listado de seguidores de un usuario con el ordenamiento descendente")
    void testGetFollowersFromSeller_WithDescOrder() throws Exception {
        int userId = 1;

        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");

        mockMvc.perform(get(USERS_PATH + "{userId}/followers/list", userId)
                        .param("order", "name_desc"))
                .andExpect(jsonPath("$.followers", Matchers.hasSize(4)))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado
                )
                .andExpect(jsonPath("$.followers[0].user_name").value("Fausto Smith"))
                .andExpect(jsonPath("$.followers[3].user_name").value("Bautista Gomez"))
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener NotFound del listado de seguidores de un usuario que no existe")
    void testGetFollowersFromSeller_WithInvalidUser() throws Exception {
        int userIdInvalid = 999;
        String expectedErrorMessage = """
                    {
                        "message": "El id ingresado no se corresponde a un user existente"
                    }
                """;

        ResultMatcher statusEsperado = status().isNotFound();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(expectedErrorMessage);

        mockMvc.perform(get(USERS_PATH + "{userId}/followers/list", userIdInvalid)
                        .param("order", "name_asc"))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado, bodyEsperado
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener BadRequest del listado de seguidores de un usuario que no es vendedor")
    void testGetFollowersFromSeller_WithNonSellerUser() throws Exception {
        int userIdNonSeller = 2;
        String expectedErrorMessage = """
                    {
                        "message": "El usuario ingresado no se trata de un vendedor y por lo tanto no puede tener seguidores"
                    }
                """;

        ResultMatcher statusEsperado = status().isBadRequest();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(expectedErrorMessage);

        mockMvc.perform(get(USERS_PATH + "{userId}/followers/list", userIdNonSeller)
                        .param("order", "name_asc"))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado, bodyEsperado
                )
                .andDo(print());
    }

}
