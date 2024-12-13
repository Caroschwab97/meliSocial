package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;

import java.util.List;

public interface IPostRepository {
    List<Post> getPosts();
}
