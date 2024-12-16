package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.dto.response.*;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.exception.InternalServerErrorException;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.exception.NotSellerException;

import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IPostRepository;
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
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    @Override
    public SellerFollowedDto getFollowersFromSeller(int sellerId, String orderMethod) {
        Optional<User> optionalUser = userRepository.getUserById(sellerId);

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
        Optional<User> optionalUser = userRepository.getUserById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("El id ingresado no se corresponde a un user existente");
        }

        User userFound = optionalUser.get();

        List<User> usersFollowedByUser = getUsersByListOfId(userFound.getFollowed());

        List<FollowedDto> usersFollowedByUserDto = getFollowedDtoSortedList(orderMethod, usersFollowedByUser);

        return new FollowedByUserDto(userFound.getId(), userFound.getUserName(), usersFollowedByUserDto);
    }

    private List<FollowedDto> getFollowedDtoSortedList(String orderMethod, List<User> usersFollowedByUser) {
        Stream<FollowedDto> usersFollowedByUserStream = usersFollowedByUser
                .stream()
                .map(
                        followed -> new FollowedDto(followed.getId(), followed.getUserName()));

        if (orderMethod.equalsIgnoreCase("name_desc")) {
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
                        userId -> userRepository.getUserById(userId)
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
        if(userRepository.getUserById(userId).isEmpty() || userRepository.getUserById(userIdToUnfollow).isEmpty())
            throw new NotFoundException("No se encontraron los usuarios");

        if(userRepository.getUserById(userId).get().getFollowed()
                .stream().filter(u -> u == userIdToUnfollow).findFirst().orElse(null) == null)
            throw new NotFoundException("El usuario no contiene ese seguido");

        if(userRepository.followedCount(userId) == 0)
            throw new NotFoundException("El usuario no tiene seguidos");

        if(!userRepository.unfollowUser(userId,userIdToUnfollow)) {
           throw new InternalServerErrorException("Ocurrió un problema al eliminar seguido");
        }
        return new ResponseDto("El usuario se borro con exito.");
    }

    @Override
    public UserFollowersDto findFollowers(int id) {
        int followersCount = userRepository.followersCount(id);
        Optional<User> user = userRepository.getUserById(id);

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

        userRepository.addFollow(userId,userIdToFollow);

        return new ResponseDto("Siguiendo al usuario: " + userRepository.getUserNameById(userIdToFollow) + " con ID: " + userIdToFollow);
    }

    @Override
    public ResponseDto addFavouritePost(int userId, int postId) {
        if (!userRepository.existsUserWithId(userId)) {
            throw new NotFoundException("El usuario con ID: " + userId + " no existe.");
        }
        if (!postRepository.existsPost(postId)) {
            throw new NotFoundException("El post con ID: " + postId + " no existe.");
        }

        User user = userRepository.getUserById(userId).get();
        if (user.getFavouritesPosts().stream().anyMatch(p -> p == postId)) {
            throw new BadRequestException("El post con id " + postId + " ya esta agregado como favorito");
        }

        userRepository.addFavouritePost(userId, postId);

        return new ResponseDto("El post fue agregado como favorito de forma exitosa");
    }

    @Override
    public ResponseDto removeFavouritePost(int userId, int postId) {
        if (!userRepository.existsUserWithId(userId)) {
            throw new NotFoundException("El usuario con ID: " + userId + " no existe.");
        }

        User user = userRepository.getUserById(userId).get();
        if (user.getFavouritesPosts().stream().noneMatch(p -> p == postId)) {
            throw new BadRequestException("El post con id " + postId + " no esta agregado como favorito para el usuario");
        }

        userRepository.removeFavouritePost(userId, postId);

        return new ResponseDto("El post fue removido como favorito de forma exitosa");
    }
}
