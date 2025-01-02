package com.spring1.meliSocial.unitTest.service;


import com.spring1.meliSocial.dto.response.*;
import com.spring1.meliSocial.exception.InternalServerErrorException;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import com.spring1.meliSocial.mapper.IMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {
    @Mock
    IUserRepository userRepository;

    @Mock
    private IMapper customMapper;

    @InjectMocks
    UserService userService;

    private int userIdToUnfollow;
    private int userIdWithUserToUnfollow;
    private User userWithUserToUnfollow;
    private User userToUnfollow;
    private User follower1;
    private User follower2;
    private User seller;

    @BeforeEach
    public void setUp() {
        // Set up tests unfollowUser
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

        // Set up tests getFollowersFromSeller
        seller = new User(1, "seller1", true, List.of(2, 3), List.of(2, 3), new ArrayList<>(), new HashSet<>());

        follower1 = new User(2, "Rocío", false, List.of(), new ArrayList<>(), new ArrayList<>(), new HashSet<>());
        follower2 = new User(3, "Bob", false, List.of(),new ArrayList<>(), new ArrayList<>(), new HashSet<>());
    }

    @Test
    public void testGetFollowersFromSeller_SortedByNameAscending() {
        Mockito.when(userRepository.getUserById(1)).thenReturn(Optional.of(seller));
        Mockito.when(userRepository.getUserById(2)).thenReturn(Optional.of(follower1));
        Mockito.when(userRepository.getUserById(3)).thenReturn(Optional.of(follower2));

        Mockito.when(customMapper.mapToFollowerDto(follower1)).thenReturn(new FollowerDto(2, "Rocío"));
        Mockito.when(customMapper.mapToFollowerDto(follower2)).thenReturn(new FollowerDto(3, "Bob"));

        SellerFollowedDto result = userService.getFollowersFromSeller(1, "name_asc");

        List<FollowerDto> followers = result.getFollowers();
        Assertions.assertEquals("Bob", followers.get(0).getUserName());
        Assertions.assertEquals("Rocío", followers.get(1).getUserName());
    }

    @Test
    public void testGetFollowersFromSeller_SortedByNameDescending() {
        Mockito.when(userRepository.getUserById(1)).thenReturn(Optional.of(seller));
        Mockito.when(userRepository.getUserById(2)).thenReturn(Optional.of(follower1));
        Mockito.when(userRepository.getUserById(3)).thenReturn(Optional.of(follower2));

        Mockito.when(customMapper.mapToFollowerDto(follower1)).thenReturn(new FollowerDto(2, "Rocío"));
        Mockito.when(customMapper.mapToFollowerDto(follower2)).thenReturn(new FollowerDto(3, "Bob"));

        SellerFollowedDto result = userService.getFollowersFromSeller(1, "name_desc");

        List<FollowerDto> followers = result.getFollowers();
        Assertions.assertEquals("Rocío", followers.get(0).getUserName());
        Assertions.assertEquals("Bob", followers.get(1).getUserName());
    }

    @Test
    public void testGetFollowedByUser_SortedByNameAscending() {
        Mockito.when(userRepository.getUserById(1)).thenReturn(Optional.of(seller));
        Mockito.when(userRepository.getUserById(2)).thenReturn(Optional.of(follower1));
        Mockito.when(userRepository.getUserById(3)).thenReturn(Optional.of(follower2));

        Mockito.when(customMapper.mapToFollowedDto(follower1)).thenReturn(new FollowedDto(2, "Rocío"));
        Mockito.when(customMapper.mapToFollowedDto(follower2)).thenReturn(new FollowedDto(3, "Bob"));

        FollowedByUserDto result = userService.getFollowedByUser(1, "name_asc");

        List<FollowedDto> followers = result.getFollowed();
        Assertions.assertEquals("Bob", followers.get(0).getUserName());
        Assertions.assertEquals("Rocío", followers.get(1).getUserName());
    }

    @Test
    public void testGetFollowedByUser_SortedByNameDescending() {
        Mockito.when(userRepository.getUserById(1)).thenReturn(Optional.of(seller));
        Mockito.when(userRepository.getUserById(2)).thenReturn(Optional.of(follower1));
        Mockito.when(userRepository.getUserById(3)).thenReturn(Optional.of(follower2));

        Mockito.when(customMapper.mapToFollowedDto(follower1)).thenReturn(new FollowedDto(2, "Rocío"));
        Mockito.when(customMapper.mapToFollowedDto(follower2)).thenReturn(new FollowedDto(3, "Bob"));

        FollowedByUserDto result = userService.getFollowedByUser(1, "name_desc");

        List<FollowedDto> followers = result.getFollowed();
        Assertions.assertEquals("Rocío", followers.get(0).getUserName());
        Assertions.assertEquals("Bob", followers.get(1).getUserName());
    }

    @Test
    public void testUnfollowUser_UserToUnfollowNotExists() {
        Optional<User> userWithUserToUnfollow = Optional.of(new User());
        Optional<User> userToUnfollow = Optional.empty();

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(userWithUserToUnfollow);
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(userToUnfollow);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void testUnfollowUser_UserWithUserToUnfollowNotExists() {
        Optional<User> userWithUserToUnfollow = Optional.empty();

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(userWithUserToUnfollow);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void testUnfollowUser_UserNotContainsAnyFollowedToUnfollow() {
        userWithUserToUnfollow.setFollowed(new ArrayList<>());

        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(Optional.of(userWithUserToUnfollow));
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.followedCount(userIdWithUserToUnfollow)).thenReturn(0);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void testUnfollowUser_UserNotContainsUserToUnfollowAsAFollowed() {
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
    public void testUnfollowUser_UserUnfollowFailedAtRepository() {
        Mockito.when(userRepository.getUserById(userIdWithUserToUnfollow)).thenReturn(Optional.of(userWithUserToUnfollow));
        Mockito.when(userRepository.getUserById(userIdToUnfollow)).thenReturn(Optional.of(userToUnfollow));
        Mockito.when(userRepository.followedCount(userIdWithUserToUnfollow)).thenReturn(3);
        Mockito.when(userRepository.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow)).thenReturn(false);

        Assertions.assertThrows(InternalServerErrorException.class, () -> {
            userService.unfollowUser(userIdWithUserToUnfollow, userIdToUnfollow);
        });
    }

    @Test
    public void testUnfollowUser_UserUnfollowedSuccessfully() {
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

    @Test
    public void testFindFollowers(){
        int id = 1;

        Mockito.when(userRepository.followersCount(id)).thenReturn(2);
        Mockito.when(userRepository.getUserById(id)).thenReturn(Optional.of(seller));
        UserFollowersDto esperado = new UserFollowersDto(id, "seller1", 2);

        UserFollowersDto obtenido = userService.findFollowers(id);
        Assertions.assertEquals(esperado, obtenido);
    }

    @Test
    public void testFindFollowersException(){
        int id = 5;

        Mockito.when(userRepository.followersCount(id)).thenReturn(-1);
        Mockito.when(userRepository.getUserById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.findFollowers(id));
    }
}
