package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;

import com.spring1.meliSocial.model.Post;

import java.util.List;

public interface IPostRepository {
    void addNewProductPromo(Post product);

    boolean findById(int id);

    List<Post> getPosts();

    int countProductsOnPromo(int userId);
}
