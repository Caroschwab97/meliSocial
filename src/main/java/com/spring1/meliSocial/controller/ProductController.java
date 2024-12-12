package com.spring1.meliSocial.controller;

import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private IProductService service;
}
