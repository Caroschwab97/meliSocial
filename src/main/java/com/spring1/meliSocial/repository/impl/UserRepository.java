package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements IUserRepository {

    private List<User> users= new ArrayList<>();

    public UserRepository() throws IOException {
        this.loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> usuarios ;

        file= ResourceUtils.getFile("classpath:user.json");
        users= objectMapper.readValue(file,new TypeReference<List<User>>(){});

    }

    @Override
    public int followersCount(int id) {
        User user = getUser(id);
        if(user != null){
            return user.getFollowers().size();
        }
        return -1;
    }

    @Override
    public User getUser(int id) {
        return users.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }
}

