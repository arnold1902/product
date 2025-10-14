package com.manager.product.repository;

import com.manager.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des produits
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Trouve tous les produits actifs
     */
    List<Product> findByActiveTrue();

    /**
     * Trouve les produits par catégorie
     */
    List<Product> findByCategoryIgnoreCase(String category);

    /**
     * Trouve les produits actifs par catégorie
     */
    List<Product> findByCategoryIgnoreCaseAndActiveTrue(String category);

    /**
     * Trouve un produit par SKU
     */
    Optional<Product> findBySku(String sku);

    /**
     * Trouve les produits dont le nom contient le texte recherché (insensible à la casse)
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Trouve les produits actifs dont le nom contient le texte recherché
     */
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    /**
     * Trouve les produits dans une gamme de prix
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Trouve les produits actifs dans une gamme de prix
     */
    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Trouve les produits avec stock faible (quantité <= seuil)
     */
    @Query("SELECT p FROM Product p WHERE p.quantityInStock <= :threshold AND p.active = true")
    List<Product> findProductsWithLowStock(@Param("threshold") Integer threshold);

    /**
     * Trouve les produits disponibles (actifs et en stock)
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.quantityInStock > 0")
    List<Product> findAvailableProducts();

    /**
     * Compte le nombre de produits par catégorie
     */
    @Query("SELECT p.category, COUNT(p) FROM Product p WHERE p.active = true GROUP BY p.category")
    List<Object[]> countProductsByCategory();

    /**
     * Vérifie si un SKU existe déjà (pour éviter les doublons)
     */
    boolean existsBySku(String sku);

    /**
     * Vérifie si un SKU existe pour un autre produit (lors de la mise à jour)
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
}