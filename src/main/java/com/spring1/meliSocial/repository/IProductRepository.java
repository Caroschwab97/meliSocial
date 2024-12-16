package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;

import java.util.List;

public interface IProductRepository {

    boolean findId(Integer id);

    List<Product> getProducts();

    void add(Product product);
}
