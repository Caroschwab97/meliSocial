package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.request.RequestPostDto;

import com.spring1.meliSocial.dto.request.ProductPromoDto;

import com.spring1.meliSocial.dto.response.PostIndexDto;
import com.spring1.meliSocial.dto.response.PostPromoDto;
import com.spring1.meliSocial.dto.response.ResponseDto;

import java.util.List;


public interface IPostService {

    ResponseDto saveNewPost(RequestPostDto requestPostDto);

    ResponseDto addNewProductPromo(ProductPromoDto product);

    PostIndexDto getPostsByUser(int userId, String order);

    PostPromoDto getProductsOnPromo(int userId);

    List<RequestPostDto> getBestProductsOnPromo(Integer category);

    ResponseDto updatePromoDiscount(int id, double discount);

    List<RequestPostDto> getAll();

    ResponseDto updatePrice(int id, double price);

}
