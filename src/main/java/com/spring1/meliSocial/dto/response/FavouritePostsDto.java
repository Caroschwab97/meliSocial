package com.spring1.meliSocial.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring1.meliSocial.dto.request.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FavouritePostsDto {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("favourite_posts")
    private List<PostDto> favouritePosts;
}