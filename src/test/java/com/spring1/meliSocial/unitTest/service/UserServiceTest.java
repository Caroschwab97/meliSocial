package com.spring1.meliSocial.unitTest.service;

import com.spring1.meliSocial.dto.response.FollowedByUserDto;
import com.spring1.meliSocial.dto.response.FollowedDto;
import com.spring1.meliSocial.dto.response.FollowerDto;
import com.spring1.meliSocial.dto.response.SellerFollowedDto;
import com.spring1.meliSocial.mapper.IMapper;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;

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

    @BeforeEach
    public void setUp() {
        User seller = new User(1, "seller1", true, List.of(2, 3), List.of(2, 3), new ArrayList<>(), new HashSet<>());

        follower1 = new User(2, "Rocío", false, List.of(), new ArrayList<>(), new ArrayList<>(), new HashSet<>());
        follower2 = new User(3, "Bob", false, List.of(),new ArrayList<>(), new ArrayList<>(), new HashSet<>());

        when(userRepository.getUserById(1)).thenReturn(Optional.of(seller));
        when(userRepository.getUserById(2)).thenReturn(Optional.of(follower1));
        when(userRepository.getUserById(3)).thenReturn(Optional.of(follower2));
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
}
