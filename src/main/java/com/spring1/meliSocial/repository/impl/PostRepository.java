package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IPostRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository implements IPostRepository {

    private List<Post> posts = new ArrayList<>();

    public PostRepository() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Post> posteos ;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // Registrar el módulo en el ObjectMapper
        objectMapper.registerModule(javaTimeModule);

        file= ResourceUtils.getFile("classpath:post.json");
        posteos= objectMapper.readValue(file,new TypeReference<List<Post>>(){});

        posts = posteos;
    }

    @Override
    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public int countProductsOnPromo(int userId) {
        List<Post> userPosts = posts.stream()
                .filter(post -> post.getUserId() == userId)
                .toList();

        if (userPosts.isEmpty()) {
            throw new BadRequestException("No existe un posteo del usuario con ese id");
        }

        long promoCount = userPosts.stream()
                .filter(Post::isHasPromo)
                .count();

        return (int) promoCount;

    }


}
