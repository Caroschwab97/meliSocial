package com.spring1.meliSocial.dto.response;

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
