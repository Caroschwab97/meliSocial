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

    void updatePromoDiscount(int id, double discount);

    boolean existsById (int id);

    List<Post> getBestProductsOnPromo();

    void updatePrice(int id, double newPrice);

}
