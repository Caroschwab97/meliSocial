package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IProductRepository;
import com.spring1.meliSocial.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository repository;
}
