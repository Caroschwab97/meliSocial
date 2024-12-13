package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FollowedByUserDto {
    @JsonAlias("user_id")
    private int userId;

    @JsonAlias("user_name")
    private String userName;

    private List<FollowedDto> followed;
}
