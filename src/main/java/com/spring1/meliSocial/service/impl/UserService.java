package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.dto.UserFollowersDto;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.model.User;
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
    public UserFollowersDto findFollowers(int id) {
        int res = repository.followersCount(id);
        User user = repository.getUser(id);
        if(res != -1){
            return new UserFollowersDto(id,user.getUserName(),res);
        }
        throw new NotFoundException("El id que busca no existe ");
    }


}
