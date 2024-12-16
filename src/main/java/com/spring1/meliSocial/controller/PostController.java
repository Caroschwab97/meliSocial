package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.request.PostDto;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.dto.request.ProductPromoDto;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("products")
public class PostController {

    @Autowired
    private IPostService service;

    @PostMapping("post")
    public ResponseEntity<?> addNewPost(@RequestBody PostDto postDto){
        service.saveNewPost(postDto);
        return new ResponseEntity<>(new ResponseDto("Publicación creada"), HttpStatus.OK);
    }

    @PostMapping("promo-post")
    public ResponseEntity<?> post(@RequestBody ProductPromoDto dto) {
        service.addNewProductPromo(dto);
        return new ResponseEntity<>(new ResponseDto("Publicación con promoción creada"), HttpStatus.OK);
    }

    @GetMapping("followed/{userId}/list")
    public ResponseEntity<?> getPostsByUser(@PathVariable int userId,
                                            @RequestParam(required = false, defaultValue = "") String order) {
        return new ResponseEntity<>(service.getPostsByUser(userId, order), HttpStatus.OK);
    }

    @GetMapping("promo-post/count")
    public ResponseEntity<?> getProductsOnPromo(@RequestParam("user_id") int userId){
        return new ResponseEntity<>(service.getProductsOnPromo(userId), HttpStatus.OK);
    }

    @GetMapping("best/promo-post")
    public ResponseEntity<?> getBestProductsOnPromo(@RequestParam(required = false) Integer category){
        return new ResponseEntity<List<PostDto>>(service.getBestProductsOnPromo(category), HttpStatus.OK);
    }
}
