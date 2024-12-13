package com.spring1.meliSocial.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.exception.ExistingDataException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IProductRepository;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IPostService;
import com.spring1.meliSocial.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository repository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public String saveNewPost(PostDto postDto) {
        Optional<User> idUser = userRepository.getUserById(postDto.getUserId());
        if(!idUser.isPresent()){
            throw new ExistingDataException("el usuario con id: " + postDto.getUserId() + " no existe");
        }
        Post post = mapper.convertValue(postDto,Post.class);
        saveNewProduct(post);
        return repository.saveNewPost(post);
    }

    public void saveNewProduct(Post post){
        Optional<Product> idProduct= productRepository.findId(post.getProduct().getId());
        if(idProduct.isPresent()){
            throw new ExistingDataException("El producto con el id " + post.getProduct().getId() + " ya existe");
        }

        List<Product> productList = productRepository.getProducts();
        productList.add(post.getProduct());
    }

}
