package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.spring1.meliSocial.dto.PostDto;
import com.spring1.meliSocial.exception.ExistingDataException;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.User;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.repository.IProductRepository;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository implements IPostRepository {

    @Autowired
    private IProductRepository productRepository;

    private int countId=0;
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
    public String saveNewPost(Post post) {
        int countId = lastId()+1;
        post.setId(countId);
        posts.add(post);
        return post.toString();
    }

    @Override
    public int lastId() {
        return posts.stream().mapToInt(Post::getId).max().orElse(0);
    }



    @Override
    public void addNewProductPromo(Post product) {
        posts.add(product);
    }

    @Override
    public boolean findById(int id) {
        if (posts.stream().filter(x ->x.getId() == id).findFirst().isPresent())
            throw new BadRequestException("el id del producto ya existe");
        return false;
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
