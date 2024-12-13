package com.spring1.meliSocial.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.dto.PostIndexDto;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IUserRepository;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PostIndexDto getPostsByUser(int userId, String order) {
        User user = userRepository.getUser(userId);

        if (user == null) {
            throw new NotFoundException("No se encontro el usuario.");
        }

        List<Integer> followedIds = user.getFollowed();

        Comparator<Post> comparator = (order.equalsIgnoreCase("date_asc"))
                ? Comparator.comparing(Post::getDate)
                : Comparator.comparing(Post::getDate).reversed();

        List<Post> filteredPosts = postRepository.getPosts().stream()
                .filter(p -> followedIds.contains(p.getUserId()))
                .filter(p -> p.getDate() != null && ChronoUnit.DAYS.between(p.getDate(), LocalDate.now()) <= 14)
                .sorted(comparator)
                .toList();

        return new PostIndexDto(userId, filteredPosts.stream()
                .map(fp -> objectMapper.convertValue(fp, PostDto.class))
                .toList());
    }
}
