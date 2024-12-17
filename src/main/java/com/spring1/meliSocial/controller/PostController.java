package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.dto.request.RequestPostDto;
import com.spring1.meliSocial.dto.response.PostIndexDto;
import com.spring1.meliSocial.dto.response.ResponseDto;
import com.spring1.meliSocial.dto.request.ProductPromoDto;
import com.spring1.meliSocial.dto.response.ResponsePostDto;
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
    public ResponseEntity<?> addNewPost(@RequestBody RequestPostDto requestPostDto){
        service.saveNewPost(requestPostDto);
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
        return new ResponseEntity<List<ResponsePostDto>>(service.getBestProductsOnPromo(category), HttpStatus.OK);
    }

    @PatchMapping("update-promo/{id}/{discount}")
    public ResponseEntity<String> updatePromoDiscount(@PathVariable int id, @PathVariable double discount) {
        service.updatePromoDiscount(id, discount);
        return new ResponseEntity<>("La promoción se actualizó correctamente", HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<ResponsePostDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PatchMapping("post/update-price/{id}/{price}")
    public ResponseEntity<?> updatePrice (@PathVariable int id, @PathVariable double price){
        return new ResponseEntity<>(service.updatePrice(id,price), HttpStatus.OK);
    }

}
