package com.spring1.meliSocial.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.spring1.meliSocial.dto.ProductPromoDto;
import com.spring1.meliSocial.exception.BadRequestException;
import com.spring1.meliSocial.model.Post;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.repository.IPostRepository;
import com.spring1.meliSocial.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository repository;

    @Override
    public void addNewProductPromo(ProductPromoDto productDto) {
        if(repository.findById(productDto.getId()))
            throw new BadRequestException("el id del producto ya existe");

        ObjectMapper objectMapper = new ObjectMapper();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // Registrar el módulo en el ObjectMapper
        objectMapper.registerModule(javaTimeModule);

        Post aux = objectMapper.convertValue(productDto, Post.class);
        repository.addNewProductPromo(aux);
    }


}
