package com.spring1.meliSocial.unitTest.service;

import com.spring1.meliSocial.dto.response.*;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.mapper.IMapper;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.repository.impl.UserRepository;
import com.spring1.meliSocial.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IMapper customMapper;

    @InjectMocks
    private UserService userService;

    private User follower1;
    private User follower2;
    private User seller;

    @BeforeEach
    public void setUp() {
        seller = new User(1, "seller1", true, List.of(2, 3), List.of(2, 3), new ArrayList<>(), new HashSet<>());

        follower1 = new User(2, "Rocío", false, List.of(), new ArrayList<>(), new ArrayList<>(), new HashSet<>());
        follower2 = new User(3, "Bob", false, List.of(),new ArrayList<>(), new ArrayList<>(), new HashSet<>());

        //when(userRepository.getUserById(1)).thenReturn(Optional.of(seller));
        //when(userRepository.getUserById(2)).thenReturn(Optional.of(follower1));
        //when(userRepository.getUserById(3)).thenReturn(Optional.of(follower2));
    }

    @Test
    public void testGetFollowersFromSeller_SortedByNameAscending() {
        when(customMapper.mapToFollowerDto(follower1)).thenReturn(new FollowerDto(2, "Rocío"));
        when(customMapper.mapToFollowerDto(follower2)).thenReturn(new FollowerDto(3, "Bob"));

        SellerFollowedDto result = userService.getFollowersFromSeller(1, "name_asc");

        List<FollowerDto> followers = result.getFollowers();
        assertEquals("Bob", followers.get(0).getUserName());
        assertEquals("Rocío", followers.get(1).getUserName());
    }

    @Test
    public void testGetFollowersFromSeller_SortedByNameDescending() {
        when(customMapper.mapToFollowerDto(follower1)).thenReturn(new FollowerDto(2, "Rocío"));
        when(customMapper.mapToFollowerDto(follower2)).thenReturn(new FollowerDto(3, "Bob"));

        SellerFollowedDto result = userService.getFollowersFromSeller(1, "name_desc");

        List<FollowerDto> followers = result.getFollowers();
        assertEquals("Rocío", followers.get(0).getUserName());
        assertEquals("Bob", followers.get(1).getUserName());
    }

    @Test
    public void testGetFollowedByUser_SortedByNameAscending() {
        when(customMapper.mapToFollowedDto(follower1)).thenReturn(new FollowedDto(2, "Rocío"));
        when(customMapper.mapToFollowedDto(follower2)).thenReturn(new FollowedDto(3, "Bob"));

        FollowedByUserDto result = userService.getFollowedByUser(1, "name_asc");

        List<FollowedDto> followers = result.getFollowed();
        assertEquals("Bob", followers.get(0).getUserName());
        assertEquals("Rocío", followers.get(1).getUserName());
    }

    @Test
    public void testGetFollowedByUser_SortedByNameDescending() {
        when(customMapper.mapToFollowedDto(follower1)).thenReturn(new FollowedDto(2, "Rocío"));
        when(customMapper.mapToFollowedDto(follower2)).thenReturn(new FollowedDto(3, "Bob"));

        FollowedByUserDto result = userService.getFollowedByUser(1, "name_desc");

        List<FollowedDto> followers = result.getFollowed();
        assertEquals("Rocío", followers.get(0).getUserName());
        assertEquals("Bob", followers.get(1).getUserName());
    }

    @Test
    @DisplayName("Verificar que el usuario a seguir exista - OK.")
    public void testFollowUser_exists() {
        int userId = follower1.getId();
        int userIdToFollow = seller.getId();

        when(userRepository.getUserById(userIdToFollow)).thenReturn(Optional.of(seller));
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(follower1));

        ResponseDto response = userService.followUser(userId, userIdToFollow);

        Assertions.assertNotNull(response);
        assertEquals("Siguiendo al usuario: null con ID: 1", response.getMessage());
    }

    @Test
    @DisplayName("Verificar que el usuario no exista - Exception.")
    public void testFollowUser_userIdNotExists() {
        int userId = 1;
        int userIdToFollow = 5;

        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        Exception r = assertThrows(NotFoundException.class, () -> {
            userService.followUser(userId, userIdToFollow);
        });
        assertEquals("El usuario con ID: " + userId + " no existe.", r.getMessage());
    }

    @Test
    @DisplayName("Verificar que la operación followUser lanza NotFoundException si el usuario a seguir no existe")
    public void testFollowUser_userToFollowNotExists() {
        int userId = 5;
        int userIdToFollow = 1;

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(new User(userId, "Test User", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashSet<>())));
        when(userRepository.getUserById(userIdToFollow)).thenReturn(Optional.empty());

        Exception r = assertThrows(NotFoundException.class, () -> {
            userService.followUser(userId, userIdToFollow);
        });
        assertEquals("El usuario a seguir con ID: " + userIdToFollow + " no existe.", r.getMessage());
    }

    @Test
    @DisplayName("Verificar que followUser lanza BadRequestException si el usuario a seguir no es vendedor")
    public void testFollowUser_userToFollowNotSeller() {
        int userId = 5;
        int userIdToFollow = 1;

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(new User(userId, "Test User", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashSet<>())));
        when(userRepository.getUserById(userIdToFollow)).thenReturn(Optional.of(new User(userId, "Test User", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashSet<>())));

        Exception r = assertThrows(BadRequestException.class, () -> {
            userService.followUser(userId, userIdToFollow);
        });
        assertEquals("Un comprador solo puede seguir a un usuario vendedor.", r.getMessage());
    }

    @Test
    @DisplayName("Verificar que followUser lanza BadRequestException si el usuario a seguir es el mismo usuario")
    public void testFollowUser_sameUserToFollow() {
        int userId = 1;
        int userIdToFollow = 1;

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(new User(userId, "Test User", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashSet<>())));
        when(userRepository.getUserById(userIdToFollow)).thenReturn(Optional.of(new User(userId, "Test User", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashSet<>())));

        Exception r = assertThrows(BadRequestException.class, () -> {
            userService.followUser(userId, userIdToFollow);
        });
        assertEquals("Un usuario no puede seguirse a sí mismo.", r.getMessage());
    }
}
