package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.request.PostDto;

import com.spring1.meliSocial.dto.request.ProductPromoDto;

import com.spring1.meliSocial.dto.response.PostIndexDto;
import com.spring1.meliSocial.dto.response.PostPromoDto;
import com.spring1.meliSocial.dto.response.ResponseDto;

import java.util.List;

public interface IPostService {

    void saveNewPost(PostDto postDto);

    void addNewProductPromo(ProductPromoDto product);

    PostIndexDto getPostsByUser(int userId, String order);

    PostPromoDto getProductsOnPromo(int userId);

    ResponseDto updatePrice(int id, double price);

}
