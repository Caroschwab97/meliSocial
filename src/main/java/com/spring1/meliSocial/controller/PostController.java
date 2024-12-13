package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private IPostService service;

    @GetMapping("/products/followed/{userId}/list")
    public ResponseEntity<?> getPostsByUser(@PathVariable int userId,
                                            @RequestParam(required = false, defaultValue = "") String order) {
        validateGetPostsByUserParams(userId, order);
        return new ResponseEntity<>(service.getPostsByUser(userId, order), HttpStatus.OK);
    }

    private void validateGetPostsByUserParams(int userId, String order) {
        if (userId == 0 ||
                (order != null && !order.isEmpty() &&
                        !order.equalsIgnoreCase("date_asc") &&
                        !order.equalsIgnoreCase("date_desc"))) {
            throw new BadRequestException("Parámetros inválidos.");
        }
    }

    @GetMapping("/products/promo-post/count")
    public ResponseEntity<?> getProductsOnPromo(@RequestParam("user_id") int userId){
        return ResponseEntity.ok(service.getProductsOnPromo(userId));
    }
}
