package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.PostDto;

import com.spring1.meliSocial.dto.ProductPromoDto;

import com.spring1.meliSocial.dto.PostIndexDto;
import com.spring1.meliSocial.dto.PostPromoDto;

public interface IPostService {

    String saveNewPost(PostDto postDto);

    void addNewProductPromo(ProductPromoDto product);

    PostIndexDto getPostsByUser(int userId, String order);

    PostPromoDto getProductsOnPromo(int userId);
}
