package com.spring1.meliSocial.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring1.meliSocial.model.Product;
import com.spring1.meliSocial.repository.IProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository implements IProductRepository {

    private List<Product> products = new ArrayList<>();

    public ProductRepository() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> productos ;

        file= ResourceUtils.getFile("classpath:product.json");
        productos= objectMapper.readValue(file,new TypeReference<List<Product>>(){});

        products=productos;
    }
}
