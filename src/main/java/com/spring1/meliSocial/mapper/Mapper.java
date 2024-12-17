package com.spring1.meliSocial.mapper;

import com.spring1.meliSocial.dto.response.ProductDto;
import com.spring1.meliSocial.dto.response.ResponsePostDto;
import com.spring1.meliSocial.model.Post;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Mapper implements IMapper{
    @Override
    public ResponsePostDto mapToResponsePostDto(Post post) {
        return new ResponsePostDto(
                post.getId(),
                post.getUserId(),
                post.getDate(),
                new ProductDto(post.getProduct().getId(),
                        post.getProduct().getName(),
                        post.getProduct().getType(),
                        post.getProduct().getBrand(),
                        post.getProduct().getColor(),
                        post.getProduct().getNotes()),
                post.getCategory(),
                post.getPrice(),
                post.isHasPromo(),
                post.getDiscount());
    }
}
