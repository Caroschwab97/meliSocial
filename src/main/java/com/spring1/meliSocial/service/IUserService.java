package com.spring1.meliSocial.service;


import com.spring1.meliSocial.dto.*;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId, String orderMethod);

    FollowedByUserDto getFollowedByUser(int userId, String orderMethod);

    UserFollowersDto findFollowers(int id);

    ResponseDto followUser(int userId, int userIdToFollow);
}
