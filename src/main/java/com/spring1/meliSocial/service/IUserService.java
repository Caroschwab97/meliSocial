package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.UserFollowersDto;

public interface IUserService {

    UserFollowersDto findFollowers(int id);
}
