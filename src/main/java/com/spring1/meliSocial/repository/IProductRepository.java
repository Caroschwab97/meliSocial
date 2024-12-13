package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;

import java.util.Optional;

public interface IProductRepository {

    Optional<Product> findId(Integer id);

    void saveNewProduct(Post post);
}
