package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.dto.response.UserFollowersDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

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
    @DisplayName("Poder realizar la acción de “Follow” (seguir) a un determinado vendedor")
    public void testFollowUserOK() throws Exception {
        ResponseDto result = new ResponseDto("Siguiendo al usuario: Agustina Lopez con ID: 1");
        int user = 4;
        int userToFollow = 1;


        ResultMatcher statusEsperado = status().isOk();
        ResultMatcher contentTypeEsperado = content().contentType("application/json");
        ResultMatcher bodyEsperado = content().json(objectMapper.writeValueAsString(result));
        {

        }
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        statusEsperado, contentTypeEsperado, bodyEsperado
                )
                .andDo(print());
    }

}
