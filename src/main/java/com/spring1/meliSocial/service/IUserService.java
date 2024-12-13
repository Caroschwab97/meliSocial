package com.spring1.meliSocial.service;


import com.spring1.meliSocial.dto.SellerFollowedDto;
import com.spring1.meliSocial.dto.UserFollowersDto;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId);

    UserFollowersDto findFollowers(int id);
}
