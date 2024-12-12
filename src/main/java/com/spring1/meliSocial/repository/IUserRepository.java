package com.spring1.meliSocial.repository;

public interface IUserRepository {
    boolean unfollowUser(int userId, int userIdToUnfollow);
}
