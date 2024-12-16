package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.spring1.meliSocial.dto.request.PostDto;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.exception.NotFoundException;
import com.spring1.meliSocial.model.Post;
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

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        objectMapper.registerModule(javaTimeModule);

        file= ResourceUtils.getFile("classpath:post.json");

        posts = objectMapper.readValue(file,new TypeReference<List<Post>>(){});;
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
        if (posts.stream().anyMatch(x ->x.getId() == id))
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
            throw new NotFoundException("No se encontraron posteos para el vendedor con ID: " + userId);
        }

        return userPosts.stream()
                .filter(Post::isHasPromo)
                .toList()
                .size();
    }

    public boolean existsById (int id){
        return posts.stream().anyMatch(value -> value.getId() == id);
    }

    @Override
    public void updatePromoDiscount(int id, double discount) {
        Post post = posts.stream().filter(value -> value.getId() == id).findFirst().get();
        post.setDiscount(discount);
        if (!post.isHasPromo())
            post.setHasPromo(true);
    }

}
