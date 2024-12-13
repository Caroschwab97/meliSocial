package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {

    private List<User> users = new ArrayList<>();

    public UserRepository() throws IOException {
        this.loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> usuarios;

        file= ResourceUtils.getFile("classpath:user.json");
        users= objectMapper.readValue(file,new TypeReference<List<User>>(){});

    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(int id) {
        return users.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public int followersCount(int id) {
        Optional<User> user = getUserById(id);
        if(user.isPresent()){
            return user.get().getFollowers().size();
        }
        return -1;
    }

    @Override
    public void addFollow(int userId, int userIdToFollow) {
        // usuario actual
        User user = getUserById(userId)
                .orElseThrow(() -> new BadRequestException("El usuario con ID " + userId + " no existe."));


        if (userId == userIdToFollow) {
            throw new BadRequestException("Un usuario no puede seguirse a sí mismo.");
        }

        if (user.getFollowed().contains(userIdToFollow)) {
            throw new BadRequestException("El usuario con ID " + userId + " ya sigue al usuario con ID " + userIdToFollow);
        }

        // usuario a seguir
        User userToFollow = getUserById(userIdToFollow)
                .orElseThrow(() -> new BadRequestException("El usuario con ID " + userIdToFollow + " no existe."));

        // usuario a seguir es un vendedor?
        if (!userToFollow.isSeller()) {
            throw new BadRequestException("Solo se puede seguir a un usuario vendedor.");
        }

        user.getFollowed().add(userIdToFollow);
        userToFollow.getFollowers().add(userId);
    }

}

