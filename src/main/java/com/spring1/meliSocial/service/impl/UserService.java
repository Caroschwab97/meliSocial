package com.spring1.meliSocial.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.*;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.exception.NotSellerException;

import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository repository;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public SellerFollowedDto getFollowersFromSeller(int sellerId, String orderMethod) {
        Optional<User> optionalUser = repository.getUserById(sellerId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("El id ingresado no se corresponde a un user existente");
        }
        User userFound = optionalUser.get();

        if(!userFound.isSeller()) {
            throw new NotSellerException("El usuario ingresado no se trata de un vendedor y por lo tanto no puede tener seguidores");
        }

        List<User> userFollowers = getUsersByListOfId(userFound.getFollowers());
        List<FollowerDto> userFollowersDto = getFollowerDtoSortedList(orderMethod, userFollowers);

        return new SellerFollowedDto(userFound.getId(), userFound.getUserName(), userFollowersDto);
    }

    private List<FollowerDto> getFollowerDtoSortedList(String orderMethod, List<User> userFollowers) {
        Stream<FollowerDto> userFollowersDtoStream = userFollowers.
                stream().
                map(
                follower -> mapper.convertValue(follower, FollowerDto.class));

        if (orderMethod.equalsIgnoreCase("name_desc")) {
            userFollowersDtoStream = userFollowersDtoStream.sorted(Comparator.comparing(FollowerDto::getUserName).reversed());
        } else {
            userFollowersDtoStream = userFollowersDtoStream.sorted(Comparator.comparing(FollowerDto::getUserName));
        }
        return userFollowersDtoStream.toList();
    }

    @Override
    public FollowedByUserDto getFollowedByUser(int userId, String orderMethod) {
        Optional<User> optionalUser = repository.getUserById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("El id ingresado no se corresponde a un user existente");
        }

        User userFound = optionalUser.get();

        List<User> usersFollowedByUser = getUsersByListOfId(userFound.getFollowed());

        List<FollowedDto> usersFollowedByUserDto = getFollowedDtoSortedList(orderMethod, usersFollowedByUser);

        return new FollowedByUserDto(userFound.getId(), userFound.getUserName(), usersFollowedByUserDto);
    }

    private List<FollowedDto> getFollowedDtoSortedList(String orderMethod, List<User> usersFollowedByUser) {
        Stream<FollowedDto> usersFollowedByUserStream = usersFollowedByUser.
                stream().
                map(
                        followed -> mapper.convertValue(followed, FollowedDto.class));

        if (orderMethod.equalsIgnoreCase("name_desc")) {
            usersFollowedByUserStream = usersFollowedByUserStream.sorted(Comparator.comparing(FollowedDto::getUserName).reversed());
        } else {
            usersFollowedByUserStream = usersFollowedByUserStream.sorted(Comparator.comparing(FollowedDto::getUserName));
        }
        return usersFollowedByUserStream.toList();
    }

    private List<User> getUsersByListOfId(List<Integer> usersId) {
        return usersId.
                stream().
                map(
                userId -> repository.getUserById(userId).get()).
                toList();
    }

    @Override
    public UserFollowersDto findFollowers(int id) {
        int res = repository.followersCount(id);
        Optional<User> user = repository.getUserById(id);
        if(res != -1){
            return new UserFollowersDto(id,user.get().getUserName(),res);
        }
        throw new NotFoundException("El id que busca no existe ");
    }

}
