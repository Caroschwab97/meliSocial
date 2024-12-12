package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.repository.IPostRepository;
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

        if(repository.getFollowsFromUser().isEmpty())
            throw new NotFoundException("NEl usuario no tiene seguidos");

        if(repository.unfollowUser(userId,userIdToUnfollow)){
            return "El usuario se borro con exito.";
        }
        return "Ocurrió un problema al eliminar seguido";
    }
}
