package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.ProductPromoDto;
import com.spring1.meliSocial.service.IPostService;
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

    @PostMapping("/products/promo-post")
    public ResponseEntity<?> post(@RequestBody ProductPromoDto dto) {
        service.addNewProductPromo(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
