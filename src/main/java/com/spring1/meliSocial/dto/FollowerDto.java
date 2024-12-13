package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerDto {
    @JsonAlias("user_id")
    private int id;
    @JsonAlias("user_name")
    private String userName;
}
