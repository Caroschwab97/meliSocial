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
        User userUnfollowed = getUser(userIdToUnfollow);
        System.out.println("4 -" + user.getFollowed());
        System.out.println("5 -" + userUnfollowed.getFollowers());

        List<Integer> updatedFollowed = listWithoutUnfolllowUser(user.getFollowed(), userIdToUnfollow);
        List<Integer> updatedFollowers = listWithoutUnfolllowUser(userUnfollowed.getFollowers(), userId);

        System.out.println("6 -" + updatedFollowed);
        System.out.println("7 -" + updatedFollowers);


        if (updatedFollowed.size() < user.getFollowed().size())
            user.setFollowed(updatedFollowed);

        if (updatedFollowers.size() < userUnfollowed.getFollowers().size())
            user.setFollowed(updatedFollowed);

        System.out.println("8 -" + getUser(userId));
        System.out.println("9 -" + getUser(userIdToUnfollow));

        return true;
    }

    public List<Integer> listWithoutUnfolllowUser(List<Integer> listUser, int UserUnfollow){
        return listUser.stream()
                .filter(id -> id != UserUnfollow)
                .collect(Collectors.toList());
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

    @Override
    public int followedCount(int userId) {
        User user = getUser(userId);
        if(user != null){
            return user.getFollowed().size();
        }
        return -1;
    }
}

