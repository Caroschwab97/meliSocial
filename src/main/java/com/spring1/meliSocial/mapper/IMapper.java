package com.spring1.meliSocial.mapper;

import com.spring1.meliSocial.dto.response.ResponsePostDto;
import com.spring1.meliSocial.model.Post;

public interface IMapper {
    ResponsePostDto mapToResponsePostDto(Post post);


}
