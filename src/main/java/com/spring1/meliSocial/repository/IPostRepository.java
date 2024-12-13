package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;

public interface IPostRepository {

    void addNewProductPromo(Post product);

    boolean findById(int id);
}
