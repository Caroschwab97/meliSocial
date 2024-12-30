package com.spring1.meliSocial.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring1.meliSocial.dto.response.ProductDto;
import com.spring1.meliSocial.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPromoDto {
    @JsonAlias("user_id")
    @NotNull(message = "El id no puede estar vacío")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private int userId;

    @NotNull(message = "La fecha no puede estar vacía")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    @Valid
    private ProductDto product;

    @NotNull(message = "El campo no puede estar vacío")
    private int category;

    @NotNull(message = "El campo no puede estar vacío")
    @DecimalMax(value = "10000000", message = "El precio máximo por proudcto es de 10.000.000")
    @DecimalMin(value = "0.01", message = "El precio mínimo por producto es de 0.01")
    private double price;

    @JsonAlias("has_promo")
    @AssertTrue(message = "El valor del campo debe ser verdadero para admitir la promo")
    private boolean hasPromo;

    @DecimalMax(value = "1", message = "El descuento máximo no puede superar el 100%")
    @DecimalMin(value = "0.01", message = "El descuento mínimo no puede ser cero")
    private double discount;
}
