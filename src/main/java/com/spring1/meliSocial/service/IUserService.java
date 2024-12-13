package com.spring1.meliSocial.service;


import com.spring1.meliSocial.dto.FollowedByUserDto;
import com.spring1.meliSocial.dto.SellerFollowedDto;
import com.spring1.meliSocial.dto.UserFollowersDto;
import com.spring1.meliSocial.dto.*;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId);
    FollowedByUserDto getFollowedByUser(int userId);
    UserFollowersDto findFollowers(int id);
    ResponseDto unfollowUser(int userId, int userIdToUnfollow);
    ResponseDto followUser(int userId, int userIdToFollow);
}
