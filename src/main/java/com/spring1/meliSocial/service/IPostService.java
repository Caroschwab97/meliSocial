package com.spring1.meliSocial.service;

import com.spring1.meliSocial.dto.PostIndexDto;
import com.spring1.meliSocial.dto.PostPromoDto;

public interface IPostService {
    PostIndexDto getPostsByUser(int userId, String order);

    PostPromoDto getProductsOnPromo(int userId);
}
