package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IUserService;
import com.spring1.meliSocial.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping("users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @PostMapping("/users/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<?> followUser(@PathVariable int userId, @PathVariable int userIdToFollow){
        return ResponseEntity.ok(service.followUser(userId, userIdToFollow));
    }
}
