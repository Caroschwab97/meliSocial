package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.dto.response.*;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.exception.InternalServerErrorException;
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
        Stream<FollowerDto> userFollowersDtoStream = userFollowers
                .stream()
                .map(
                follower -> new FollowerDto(follower.getId(), follower.getUserName()));

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
        if (orderMethod != null && !orderMethod.isEmpty() &&
                !orderMethod.equalsIgnoreCase("name_asc") &&
                !orderMethod.equalsIgnoreCase("name_desc")) {
            throw new BadRequestException("Parámetros inválidos.");
        }

        Stream<FollowedDto> usersFollowedByUserStream = usersFollowedByUser
                .stream()
                .map(followed -> new FollowedDto(followed.getId(), followed.getUserName()));

        if (orderMethod != null && orderMethod.equalsIgnoreCase("name_desc")) {
            usersFollowedByUserStream = usersFollowedByUserStream.sorted(Comparator.comparing(FollowedDto::getUserName).reversed());
        } else {
            usersFollowedByUserStream = usersFollowedByUserStream.sorted(Comparator.comparing(FollowedDto::getUserName));
        }
        return usersFollowedByUserStream.toList();
    }

    private List<User> getUsersByListOfId(List<Integer> usersId) {
        return usersId
                .stream()
                .map(
                        userId -> repository.getUserById(userId)
                )
                .filter(
                        Optional::isPresent
                )
                .map(
                        Optional::get
                )
                .toList();
    }

    @Override
    public ResponseDto unfollowUser(int userId, int userIdToUnfollow) {
        if(repository.getUserById(userId).isEmpty() || repository.getUserById(userIdToUnfollow).isEmpty())
            throw new NotFoundException("No se encontraron los usuarios");

        if(repository.getUserById(userId).get().getFollowed()
                .stream().filter(u -> u == userIdToUnfollow).findFirst().orElse(null) == null)
            throw new NotFoundException("El usuario no contiene ese seguido");

        if(repository.followedCount(userId) == 0)
            throw new NotFoundException("El usuario no tiene seguidos");

        if(!repository.unfollowUser(userId,userIdToUnfollow)) {
           throw new InternalServerErrorException("Ocurrió un problema al eliminar seguido");
        }
        return new ResponseDto("El usuario se borro con exito.");
    }

    @Override
    public UserFollowersDto findFollowers(int id) {
        int followersCount = repository.followersCount(id);
        Optional<User> user = repository.getUserById(id);

        if(user.isEmpty()) {
            throw new NotFoundException("El id que busca no existe");
        }

        return new UserFollowersDto(id,user.get().getUserName(), followersCount);
    }

    @Override
    public ResponseDto followUser(int userId, int userIdToFollow) {
        if (userId == userIdToFollow) {
            throw new BadRequestException("Un usuario no puede seguirse a sí mismo.");
        }

        repository.addFollow(userId,userIdToFollow);

        return new ResponseDto("Siguiendo al usuario: " + repository.getUserNameById(userIdToFollow) + " con ID: " + userIdToFollow);
    }
}
