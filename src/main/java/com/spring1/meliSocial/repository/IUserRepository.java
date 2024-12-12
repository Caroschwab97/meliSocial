package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.User;

public interface IUserRepository {
    int followersCount(int id);
    User getUser(int id);
}
