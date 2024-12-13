package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.UserFollowersDto;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("{userId}/followers/list")
    public ResponseEntity<?> getFollowersFromSeller(@PathVariable int userId, @RequestParam(value = "order", defaultValue = "name_asc") String orderMethod) {
        return new ResponseEntity<>(userService.getFollowersFromSeller(userId, orderMethod), HttpStatus.OK);
    }

    @GetMapping("{userId}/followed/list")
    public ResponseEntity<?> getFollowedByUser(@PathVariable int userId, @RequestParam(value = "order", defaultValue = "name_asc") String orderMethod) {
        return new ResponseEntity<>(userService.getFollowedByUser(userId, orderMethod), HttpStatus.OK);
    }

    @GetMapping("{userId}/followers/count")
    public ResponseEntity<UserFollowersDto> getFollowerCount(@PathVariable int userId) {
        return ResponseEntity.ok(userService.findFollowers(userId));
    }
}
