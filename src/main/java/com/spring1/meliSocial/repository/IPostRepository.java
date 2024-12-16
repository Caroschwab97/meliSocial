package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;

import java.util.List;

public interface IPostRepository {

    String saveNewPost(Post post);

    int lastId();

    void addNewProductPromo(Post product);

    boolean findById(int id);

    List<Post> getPosts();

    int countProductsOnPromo(int userId);

    boolean existsById(int id);

    void updatePrice(int id, double newPrice);

}
