package com.spring1.meliSocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String userName;
    private boolean seller;
    private List<User> followers;
    private List<User> followed;
    private List<Product> products;

}
