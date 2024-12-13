package com.spring1.meliSocial.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.*;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.exception.NotSellerException;

import com.spring1.meliSocial.exception.NotFoundException;

import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository repository;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public SellerFollowedDto getFollowersFromSeller(int sellerId) {
        Optional<User> optionalUser = repository.getUserById(sellerId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("El id ingresado no se corresponde a un user existente");
        }
        User userFound = optionalUser.get();

        if(!userFound.isSeller()) {
            throw new NotSellerException("El usuario ingresado no se trata de un vendedor y por lo tanto no puede tener seguidores");
        }

        List<User> userFollowers = getUsersByListOfId(userFound.getFollowers());
        List<FollowerDto> userFollowersDto = userFollowers.
                stream().
                map(
                follower -> mapper.convertValue(follower, FollowerDto.class)).
                toList();

        return new SellerFollowedDto(userFound.getId(), userFound.getUserName(), userFollowersDto);
    }

    @Override
    public FollowedByUserDto getFollowedByUser(int userId) {
        Optional<User> optionalUser = repository.getUserById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("El id ingresado no se corresponde a un user existente");
        }

        User userFound = optionalUser.get();

        List<User> usersFollowedByUser = getUsersByListOfId(userFound.getFollowed());

        List<FollowedDto> usersFollowedByUserDto = usersFollowedByUser.
                stream().
                map(
                        followed -> mapper.convertValue(followed, FollowedDto.class)).
                toList();

        return new FollowedByUserDto(userFound.getId(), userFound.getUserName(), usersFollowedByUserDto);
    }

    private List<User> getUsersByListOfId(List<Integer> usersId) {
        return usersId.
                stream().
                map(
                userId -> repository.getUserById(userId).get()).
                toList();
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

        if(repository.unfollowUser(userId,userIdToUnfollow))
            return new ResponseDto("El usuario se borro con exito.");

        return new ResponseDto("Ocurrió un problema al eliminar seguido");
    }

    @Override
    public UserFollowersDto findFollowers(int id) {
        int res = repository.followersCount(id);
        Optional<User> user = repository.getUserById(id);
        if(res != -1){
            return new UserFollowersDto(id,user.get().getUserName(),res);
        }
        throw new NotFoundException("El id que busca no existe");
    }


    @Override
    public ResponseDto followUser(int userId, int userIdToFollow) {
        repository.addFollow(userId,userIdToFollow);
        return new ResponseDto("Siguiendo a " + userIdToFollow);
    }
}
