package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"product_id", "product_name", "type", "brand", "color", "notes"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {
    @JsonAlias("id")
    private int product_id;
    @JsonAlias("name")
    private String product_name;
    private String type;
    private String brand;
    private String color;
    private String notes;
}
