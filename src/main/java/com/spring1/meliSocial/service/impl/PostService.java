package com.spring1.meliSocial.service.impl;

import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository repository;
}
