package com.spring1.meliSocial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowersDto {
    private int userID;
    private String userName;
    private int followersCount;
}
