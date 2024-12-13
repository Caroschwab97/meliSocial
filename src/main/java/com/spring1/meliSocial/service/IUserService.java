package com.spring1.meliSocial.service;


import com.spring1.meliSocial.dto.response.FollowedByUserDto;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.dto.response.SellerFollowedDto;
import com.spring1.meliSocial.dto.response.UserFollowersDto;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId, String orderMethod);

    FollowedByUserDto getFollowedByUser(int userId, String orderMethod);

    UserFollowersDto findFollowers(int id);

    ResponseDto unfollowUser(int userId, int userIdToUnfollow);

    ResponseDto followUser(int userId, int userIdToFollow);
}
