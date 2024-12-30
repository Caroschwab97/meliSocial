package com.spring1.meliSocial.unitTest.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.exception.InternalServerErrorException;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IUserService;
import com.spring1.meliSocial.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {
    @Mock
    IUserRepository userRepository;

    @InjectMocks
    UserService userService;

    private int userIdToUnfollow;
    private int userIdWithUserToUnfollow;
    private User userWithUserToUnfollow;
    private User userToUnfollow;

    @BeforeEach
    public void setUp() {
        userIdToUnfollow = 100;
        userIdWithUserToUnfollow = 10;

        List<Integer> followers = new ArrayList<>();
        List<Integer> followed = new ArrayList<>(List.of(
                userIdToUnfollow,
                15,
                22
        ));
        List<Integer> products = new ArrayList<>();
        Set<Integer> favouritePosts = new HashSet<>();

        userWithUserToUnfollow = new User(
                userIdWithUserToUnfollow,
                "User 1",
                false,
                followers,
                followed,
                products,
                favouritePosts);

        List<Integer> followersUserToUnfollow = new ArrayList<>(List.of(
                userIdWithUserToUnfollow
        ));
        List<Integer> followedUserToUnfollow = new ArrayList<>();
        List<Integer> productsUserToUnfollow = new ArrayList<>();
        Set<Integer> favouritePostsUserToUnfollow = new HashSet<>();

        userToUnfollow = new User(
                userIdToUnfollow,
                "User 2",
                true,
                followersUserToUnfollow,
                followedUserToUnfollow,
                productsUserToUnfollow,
                favouritePostsUserToUnfollow);
    }

    @Test
    public void user_to_unfollow_not_exists() {
        Optional<User> userWithUserToUnfollow = Optional.of(new User());
        Optional<User> userToUnfollow = Optional.empty();

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(userWithUserToUnfollow);
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(userToUnfollow);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void user_with_user_to_unfollow_not_exists() {
        Optional<User> userWithUserToUnfollow = Optional.empty();

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(userWithUserToUnfollow);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void user_not_contains_any_followed_to_unfollow() {
        userWithUserToUnfollow.setFollowed(new ArrayList<>());

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(Optional.of(userWithUserToUnfollow));
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.followedCount(userIdWithUserToUnfollow)).thenReturn(0);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void user_not_contains_user_to_unfollow_as_a_followed() {
        userWithUserToUnfollow.setFollowed(new ArrayList<>(List.of(
                15,
                22,
                23
        )));

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(Optional.of(userWithUserToUnfollow));
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(Optional.of(userToUnfollow));
        Mockito.when(userRepository.followedCount(userIdWithUserToUnfollow)).thenReturn(3);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void user_unfollow_failed_at_repository() {
        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(Optional.of(userWithUserToUnfollow));
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(Optional.of(userToUnfollow));
        Mockito.when(userRepository.followedCount(userIdWithUserToUnfollow)).thenReturn(3);
        Mockito.when(userRepository.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow)).thenReturn(false);

        Assertions.assertThrows(InternalServerErrorException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void user_unfollowed_successfully() {
        String expectedMessage = "El usuario dejó de seguir a " + userToUnfollow.getUserName();

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(Optional.of(userWithUserToUnfollow));
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(Optional.of(userToUnfollow));
        Mockito.when(userRepository.followedCount(userIdWithUserToUnfollow)).thenReturn(3);
        Mockito.when(userRepository.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow)).thenReturn(true);
        Mockito.when(userRepository.getUserNameById(userIdToUnfollow)).thenReturn(userToUnfollow.getUserName());

        ResponseDto responseUnfollow = userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);

        Assertions.assertNotNull(responseUnfollow);
        Assertions.assertEquals(expectedMessage, responseUnfollow.getMessage());
    }
}
