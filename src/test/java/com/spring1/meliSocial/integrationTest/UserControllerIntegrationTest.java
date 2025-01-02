package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.dto.response.UserFollowersDto;
import org.junit.jupiter.api.DisplayName;
import com.spring1.meliSocial.dto.response.FollowedByUserDto;
import com.spring1.meliSocial.dto.response.FollowedDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
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

    private FollowedDto followed1;
    private FollowedDto followed2;

    @Test
    @DisplayName("Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor")
    public void testGetFollowerCountOK() throws Exception {
        int parametroEntrada = 1;
        UserFollowersDto result = new UserFollowersDto(1, "Agustina Lopez", 4);

        mockMvc.perform(get("/users/{userId}/followers/count", parametroEntrada))
                .andExpectAll(
                        status().isOk(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Obtener NotFound de la cantidad de usuarios que siguen a un determinado vendedor")
    public void testGetFollowerCountNotFound() throws Exception {
        int parametroEntrada = 10;
        ResponseDto result = new ResponseDto("El id que busca no existe");

        mockMvc.perform(get("/users/{userId}/followers/count", parametroEntrada))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("Poder realizar la acción de “Follow” (seguir) a un determinado vendedor")
    public void testFollowUserOK() throws Exception {
        ResponseDto result = new ResponseDto("Siguiendo al usuario: Agustina Lopez con ID: 1");
        int user = 4;
        int userToFollow = 1;

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isOk(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("“Follow” (seguir) a un determinado vendedor NotFound user")
    public void testFollowUserNotFoundUser() throws Exception {
        int user = 40;
        int userToFollow = 1;
        ResponseDto result = new ResponseDto("El usuario con ID: " + user + " no existe.");

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("“Follow” (seguir) a un determinado vendedor NotFound userToFollow")
    public void testFollowUserNotFoundUserToFollow() throws Exception {
        int user = 4;
        int userToFollow = 100;
        ResponseDto result = new ResponseDto("El usuario a seguir con ID: " + userToFollow + " no existe.");

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("“Follow” (seguir) a un determinado vendedor con ID iguales")
    public void testFollowUserBadRequestExceptionEqualsID() throws Exception {
        int user = 4;
        int userToFollow = 4;
        ResponseDto result = new ResponseDto("Un usuario no puede seguirse a sí mismo.");

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("“Follow” (seguir) a un determinado vendedor - ya contiene ese seguidor")
    public void testFollowUserBadRequestExceptionRepeatFollower() throws Exception {
        int user = 2;
        int userToFollow = 1;
        ResponseDto result = new ResponseDto("El usuario con ID: " + user + " ya sigue al usuario con ID: " + userToFollow);

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }


    @Test
    @DisplayName("“Follow” (seguir) a un determinado vendedor - el usuario a seguir no es vendedor")
    public void testFollowUserBadRequestExceptionNoASeller() throws Exception {
        int user = 2;
        int userToFollow = 3;
        ResponseDto result = new ResponseDto("Un comprador solo puede seguir a un usuario vendedor.");

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result))
                )
                .andDo(print());
    }

    @Test
    @DisplayName("“Follow” (seguir) a un determinado vendedor - el usuario a seguir no es vendedor")
    public void testFollowUserBadRequestExceptionNoASeller2() throws Exception {
        int user = 1;
        int userToFollow = 2;
        ResponseDto result = new ResponseDto("Solo se puede seguir a un usuario vendedor.");

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",user,userToFollow))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType("application/json"),
                        content().json(objectMapper.writeValueAsString(result)))
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
