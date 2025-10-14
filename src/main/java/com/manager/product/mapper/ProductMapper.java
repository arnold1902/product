package com.manager.product.mapper;

import com.manager.product.dto.CreateProductDto;
import com.manager.product.dto.ProductResponseDto;
import com.manager.product.dto.UpdateProductDto;
import com.manager.product.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper pour la conversion entre Product et ses DTOs
 */
@Component
public class ProductMapper {

    /**
     * Convertit CreateProductDto en entité Product
     */
    public Product toEntity(CreateProductDto dto) {
        if (dto == null) {
            return null;
        }

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantityInStock(dto.getQuantityInStock())
                .category(dto.getCategory())
                .sku(dto.getSku())
                .active(dto.getActive())
                .build();
    }

    /**
     * Convertit Product en ProductResponseDto
     */
    public ProductResponseDto toResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .category(product.getCategory())
                .sku(product.getSku())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .available(product.isAvailable())
                .build();
    }

    /**
     * Met à jour une entité Product à partir d'un UpdateProductDto
     */
    public void updateEntityFromDto(UpdateProductDto dto, Product product) {
        if (dto == null || product == null) {
            return;
        }

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getQuantityInStock() != null) {
            product.setQuantityInStock(dto.getQuantityInStock());
        }
        if (dto.getCategory() != null) {
            product.setCategory(dto.getCategory());
        }
        if (dto.getSku() != null) {
            product.setSku(dto.getSku());
        }
        if (dto.getActive() != null) {
            product.setActive(dto.getActive());
        }
    }
}