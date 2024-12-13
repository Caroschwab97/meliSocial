package com.spring1.meliSocial.service;


import com.spring1.meliSocial.dto.*;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId);

    FollowedByUserDto getFollowedByUser(int userId);

    UserFollowersDto findFollowers(int id);

    ResponseDto followUser(int userId, int userIdToFollow);
}
