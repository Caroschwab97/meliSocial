package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService service;

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<?> unfollowUser(@PathVariable int userId, @PathVariable int userIdToUnfollow){
        return new ResponseEntity<String>(service.unfollowUser(userId,userIdToUnfollow), HttpStatus.OK);
    }

}
