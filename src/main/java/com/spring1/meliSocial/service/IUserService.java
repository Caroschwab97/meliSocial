package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.UserDto;
import com.spring1.meliSocial.model.User;

import java.util.List;

public interface IUserService {

    String followUser(int userId, int userIdToFollow);
}
