package com.manager.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité Product représentant un produit dans le système
 * Les validations sont gérées au niveau des DTOs, pas de l'entité
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "sku", unique = true, length = 20)
    private String sku;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Vérifie si le produit est disponible en stock
     */
    public boolean isAvailable() {
        return active && quantityInStock != null && quantityInStock > 0;
    }

    /**
     * Réduit la quantité en stock
     */
    public void reduceStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        if (quantityInStock < quantity) {
            throw new IllegalStateException("Stock insuffisant");
        }
        this.quantityInStock -= quantity;
    }

    /**
     * Augmente la quantité en stock
     */
    public void increaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        this.quantityInStock += quantity;
    }
}