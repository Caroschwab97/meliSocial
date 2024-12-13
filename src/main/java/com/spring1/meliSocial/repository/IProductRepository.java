package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {

    Optional<Product> findId(Integer id);

    List<Product> getProducts();
}
