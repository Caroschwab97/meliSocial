package com.spring1.meliSocial.repository;

import com.spring1.meliSocial.model.Post;

import java.util.List;
import java.util.Optional;

public interface IPostRepository {

    String saveNewPost(Post post);

    int lastId();
}
