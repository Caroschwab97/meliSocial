package com.spring1.meliSocial.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private int id;
    @JsonAlias("user_id")
    private int userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;
    private ProductDto product;
    private int category;
    private double price; //precio original
    private boolean hasPromo;
    private double discount;
}
