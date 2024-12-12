package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.dto.UserFollowersDto;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Override
    public String unfollowUser(int userId, int userIdToUnfollow) {
        if(repository.getUser(userId) == null || repository.getUser(userId) == null)
            throw new NotFoundException("No se encontraron los usuarios");

        if(repository.followersCount(userId) == 0)
            throw new NotFoundException("El usuario no tiene seguidos");

        if(repository.unfollowUser(userId,userIdToUnfollow)){
            return "El usuario se borro con exito.";
        }
        return "Ocurrió un problema al eliminar seguido";
    }

    @Override
    public UserFollowersDto findFollowers(int id) {
        int res = repository.followersCount(id);
        User user = repository.getUser(id);
        if(res != -1){
            return new UserFollowersDto(id,user.getUserName(),res);
        }
        throw new NotFoundException("El id que busca no existe");
    }


}
