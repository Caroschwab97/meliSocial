package com.spring1.meliSocial.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowedDto {
    @JsonProperty("user_id")
    private int id;

    @JsonProperty("user_name")
    private String userName;
}
