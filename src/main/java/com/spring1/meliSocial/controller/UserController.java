package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.UserFollowersDto;
import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping("/users/{userId}/followers/count")
    public ResponseEntity<UserFollowersDto> get(@PathVariable int userId) {
        return ResponseEntity.ok(service.findFollowers(userId));
    }
}
