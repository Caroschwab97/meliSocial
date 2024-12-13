package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerFollowedDto {
    @JsonAlias("user_id")
    private int userId;
    @JsonAlias("user_name")
    private String userName;
    private List<FollowerDto> followers;
}
