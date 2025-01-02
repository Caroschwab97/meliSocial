package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.response.FollowedByUserDto;
import com.spring1.meliSocial.dto.response.FollowedDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.ResultMatcher;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.dto.response.UserFollowersDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();


    private FollowedDto followed1;
    private FollowedDto followed2;


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
        FollowedByUserDto esperado = new FollowedByUserDto(3,"Carlos Perez",seguidos);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEsperado = objectMapper.writeValueAsString(esperado);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list" , 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(jsonEsperado));
    }

    @Test
    public void testFollowedListNotFoundID() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list" , 100))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testFollowedListAsc() throws Exception {
        followed1 = new FollowedDto(1, "Agustina Lopez");
        followed2 = new FollowedDto(6, "Fausto Smith");

        List<FollowedDto> seguidos = new ArrayList<>(List.of(followed1, followed2));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEsperado = objectMapper.writeValueAsString(seguidos);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list" , 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed", Matchers.hasSize(seguidos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[0].user_id").value(seguidos.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[0].user_name").value(seguidos.get(0).getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[1].user_id").value(seguidos.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[1].user_name").value(seguidos.get(1).getUserName()));
    }

    @Test
    public void testFollowedListDesc() throws Exception {
        followed1 = new FollowedDto(1, "Agustina Lopez");
        followed2 = new FollowedDto(6, "Fausto Smith");

        List<FollowedDto> seguidos = new ArrayList<>(List.of(followed2, followed1));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEsperado = objectMapper.writeValueAsString(seguidos);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{studentId}/followed/list" , 5).param("order", "name_desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed", Matchers.hasSize(seguidos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[0].user_id").value(seguidos.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[0].user_name").value(seguidos.get(0).getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[1].user_id").value(seguidos.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed[1].user_name").value(seguidos.get(1).getUserName()));
    }

}
