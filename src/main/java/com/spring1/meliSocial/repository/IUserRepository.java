package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.User;

public interface IUserRepository {
    boolean unfollowUser(int userId, int userIdToUnfollow);
    int followersCount(int id);
    int followedCount(int userId);
    User getUser(int id);}
