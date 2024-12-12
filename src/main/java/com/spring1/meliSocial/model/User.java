package com.spring1.meliSocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String userName;
    private boolean seller;
    private List<Integer> followers;
    private List<Integer> followed;
    private List<Integer> products;

}
