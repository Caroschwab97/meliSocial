package com.spring1.meliSocial.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.exception.ExistingDataException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IProductRepository;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository repository;

    private IProductRepository productRepository;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public String saveNewPost(PostDto postDto) {

/*        Optional<Post> idPost= repository.findId(postDto.getId());
        if(idPost.isPresent()){
            throw new ExistingDataException("El Post con el id " + postDto.getId() + " ya está creado");
        }*/

        Post post = mapper.convertValue(postDto,Post.class);

        return repository.saveNewPost(post);
    }

    public void saveNewProduct(PostDto postDto){
        Optional<Product> idProduct= productRepository.findId(postDto.getProduct().getId());
        if(idProduct.isPresent()){
            throw new ExistingDataException("El producto con el id " + postDto.getProduct().getId() + " ya existe");
        }


    }


}
