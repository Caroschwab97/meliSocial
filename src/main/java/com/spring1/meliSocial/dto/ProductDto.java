package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @JsonAlias("product_id")
    private int id;
    @JsonAlias("product_name")
    private String name;
    private String type;
    private String brand;
    private String color;
    private String notes;
}
