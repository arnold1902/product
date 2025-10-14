package com.manager.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO pour la mise à jour d'un produit
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductDto {

    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal price;

    private Integer quantityInStock;

    @Size(max = 50, message = "La catégorie ne peut pas dépasser 50 caractères")
    private String category;

    @Size(max = 20, message = "Le SKU ne peut pas dépasser 20 caractères")
    private String sku;

    private Boolean active;
}