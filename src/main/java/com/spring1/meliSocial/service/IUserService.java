package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.SellerFollowedDto;

public interface IUserService {
    SellerFollowedDto getFollowersFromSeller(int sellerId);
}
