package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.dto.ResponseDto;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private IPostService service;

    @PostMapping("products/post")
    public ResponseEntity<?> addNewPost(@RequestBody PostDto postDto){
        service.saveNewPost(postDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Publicación creada"));

    }
}
