package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.PostIndexDto;

public interface IPostService {
    PostIndexDto getPostsByUser(int userId, String order);
}
