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
        System.out.println("1 -" + repository.getUser(userId));
        System.out.println("2 -" + repository.getUser(userIdToUnfollow));
        if(repository.getUser(userId).getFollowed()
                .stream().filter(u -> u == userIdToUnfollow).findFirst().orElse(null) == null)
            throw new NotFoundException("El usuario no contiene ese seguido");

        if(repository.getUser(userId) == null || repository.getUser(userIdToUnfollow) == null)
            throw new NotFoundException("No se encontraron los usuarios");

        System.out.println("3 -" + repository.followedCount(userId));

        if(repository.followedCount(userId) == 0)
            throw new NotFoundException("El usuario no tiene seguidos");

        if(repository.unfollowUser(userId,userIdToUnfollow))
            return "El usuario se borro con exito.";

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
