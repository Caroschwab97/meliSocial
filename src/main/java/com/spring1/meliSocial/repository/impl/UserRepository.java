package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepository implements IUserRepository {

    private List<User> users = new ArrayList<>();

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
    public boolean unfollowUser(int userId, int userIdToUnfollow) {
        User user = getUser(userId);
        List<Integer> updatedFollowed = user.getFollowed().stream()
                .filter(id -> id != userIdToUnfollow)
                .collect(Collectors.toList());
        boolean isUnfollowed = updatedFollowed.size() < user.getFollowed().size();
        if (isUnfollowed) {
            user.setFollowed(updatedFollowed);
        }
        return isUnfollowed;
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

