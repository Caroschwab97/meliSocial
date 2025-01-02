package com.spring1.meliSocial.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.response.FollowedByUserDto;
import com.spring1.meliSocial.dto.response.FollowedDto;
import com.spring1.meliSocial.dto.response.FollowerDto;
import com.spring1.meliSocial.dto.response.SellerFollowedDto;
import com.spring1.meliSocial.model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    private FollowedDto followed1;
    private FollowedDto followed2;

    @BeforeEach
    public void setUp() {
        followed1 = new FollowedDto(1, "Agustina Lopez");
        followed2 = new FollowedDto(6, "Fausto Smith");
    }

    @Test
    public void testFollowedList() throws Exception {
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
