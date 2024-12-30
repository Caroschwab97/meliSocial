package com.spring1.meliSocial.unitTest.service;

import com.spring1.meliSocial.dto.response.PostIndexDto;
import com.spring1.meliSocial.dto.response.ResponsePostDto;
import com.spring1.meliSocial.mapper.IMapper;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.impl.PostRepository;
import com.spring1.meliSocial.repository.impl.UserRepository;
import com.spring1.meliSocial.service.impl.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    IMapper customMapper;

    @InjectMocks
    PostService postService;

    @Test
    @DisplayName("6- Verificar el correcto ordenamiento ascendente")
    public void testGetPostsByUser_OrderAscOK() {
        //arr
        int userId = 9;
        String order = "date_asc";

        Optional<User> userEsperado = Optional.of(new User(9, "Lucia Quezada", false, List.of(), List.of(8), List.of(), Set.of()
        ));
        Post post1 = new Post(17, 8, LocalDate.parse("2024-12-17"), new Product(), 2, 20.99, true, 0.3
        );
        Post post2 = new Post(18, 8, LocalDate.parse("2024-12-27"), new Product(), 55, 15.99, true, 0.5
        );
        List<Post> postsEsperados = List.of(post1, post2);

        when(userRepository.getUserById(userId)).thenReturn(userEsperado);
        when(postRepository.getPosts()).thenReturn(postsEsperados);
        when(customMapper.mapToResponsePostDto(post1)).thenReturn(new ResponsePostDto(17, 8, LocalDate.parse("2024-12-17"), null, 2, 20.99, true, 0.3));
        when(customMapper.mapToResponsePostDto(post2)).thenReturn(new ResponsePostDto(18, 8, LocalDate.parse("2024-12-27"), null, 55, 15.99, true, 0.5));


        PostIndexDto resultado = postService.getPostsByUser(userId, order);

        List<ResponsePostDto> posteos = resultado.getPosts();
        assertNotNull(posteos);
        assertEquals(2, posteos.size());
        assertEquals(17, posteos.get(0).getId());
        assertEquals(18, posteos.get(1).getId());
        assertEquals(LocalDate.parse("2024-12-17"), posteos.get(0).getDate());
        assertEquals(LocalDate.parse("2024-12-27"), posteos.get(1).getDate());
    }

    @Test
    @DisplayName("6.1- Verificar el correcto ordenamiento descendente")
    public void testGetPostsByUser_OrderDescOK() {
        //arr
        int userId = 9;
        String order = "date_desc";

        Optional<User> userEsperado = Optional.of(new User(9, "Lucia Quezada", false, List.of(), List.of(8), List.of(), Set.of()
        ));
        Post post1 = new Post(17, 8, LocalDate.parse("2024-12-17"), new Product(), 2, 20.99, true, 0.3
        );
        Post post2 = new Post(18, 8, LocalDate.parse("2024-12-27"), new Product(), 55, 15.99, true, 0.5
        );
        List<Post> postsEsperados = List.of(post1, post2);

        when(userRepository.getUserById(userId)).thenReturn(userEsperado);
        when(postRepository.getPosts()).thenReturn(postsEsperados);
        when(customMapper.mapToResponsePostDto(post1)).thenReturn(new ResponsePostDto(17, 8, LocalDate.parse("2024-12-17"), null, 2, 20.99, true, 0.3));
        when(customMapper.mapToResponsePostDto(post2)).thenReturn(new ResponsePostDto(18, 8, LocalDate.parse("2024-12-27"), null, 55, 15.99, true, 0.5));


        PostIndexDto resultado = postService.getPostsByUser(userId, order);

        List<ResponsePostDto> posteos = resultado.getPosts();
        assertNotNull(posteos);
        assertEquals(2, posteos.size());
        assertEquals(18, posteos.get(0).getId());
        assertEquals(17, posteos.get(1).getId());
        assertEquals(LocalDate.parse("2024-12-27"), posteos.get(0).getDate());
        assertEquals(LocalDate.parse("2024-12-17"), posteos.get(1).getDate());
    }
}