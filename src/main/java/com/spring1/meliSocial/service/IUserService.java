package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.UserFollowersDto;

public interface IUserService {
    String unfollowUser(int userId, int userIdToUnfollow);

    UserFollowersDto findFollowers(int id);
}
