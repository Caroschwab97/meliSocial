package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"user_id", "post_id", "date", "product", "category", "price"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {
    @JsonAlias("userId")
    private int user_id;
    @JsonAlias("id")
    private int post_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;
    private ProductDto product;
    private int category;
    private double price;
}
