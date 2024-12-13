package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.UserFollowersDto;
import com.spring1.meliSocial.service.IUserService;
import com.spring1.meliSocial.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("{userId}/followers/list")
    public ResponseEntity<?> getFollowersFromSeller(@PathVariable int userId) {
        return new ResponseEntity<>(userService.getFollowersFromSeller(userId), HttpStatus.OK);
    }

    @GetMapping("{userId}/followed/list")
    public ResponseEntity<?> getFollowedByUser(@PathVariable int userId) {
        return new ResponseEntity<>(userService.getFollowedByUser(userId), HttpStatus.OK);
    }

    @GetMapping("{userId}/followers/count")
    public ResponseEntity<UserFollowersDto> getFollowerCount(@PathVariable int userId) {
        return ResponseEntity.ok(userService.findFollowers(userId));
    }
    private IUserService service;

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<?> followUser(@PathVariable int userId, @PathVariable int userIdToFollow){
        return ResponseEntity.ok(userService.followUser(userId, userIdToFollow));
    }
}
