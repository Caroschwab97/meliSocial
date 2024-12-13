package com.spring1.meliSocial.service;


import com.spring1.meliSocial.dto.FollowedByUserDto;
import com.spring1.meliSocial.dto.FollowedDto;
import com.spring1.meliSocial.dto.SellerFollowedDto;
import com.spring1.meliSocial.dto.UserFollowersDto;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId, String orderMethod);

    FollowedByUserDto getFollowedByUser(int userId, String orderMethod);

    UserFollowersDto findFollowers(int id);
}
