package com.manager.product.controller;

import com.manager.product.dto.CreateProductDto;
import com.manager.product.dto.ProductResponseDto;
import com.manager.product.dto.UpdateProductDto;
import com.manager.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des produits
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    /**
     * GET /api/v1/products - Récupère tous les produits avec pagination
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("GET /api/v1/products - Récupération de tous les produits");
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/v1/products/active - Récupère tous les produits actifs
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveProducts() {
        log.debug("GET /api/v1/products/active - Récupération des produits actifs");
        List<ProductResponseDto> products = productService.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/v1/products/{id} - Récupère un produit par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.debug("GET /api/v1/products/{} - Récupération du produit", id);
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * GET /api/v1/products/sku/{sku} - Récupère un produit par son SKU
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDto> getProductBySku(@PathVariable String sku) {
        log.debug("GET /api/v1/products/sku/{} - Récupération du produit", sku);
        ProductResponseDto product = productService.getProductBySku(sku);
        return ResponseEntity.ok(product);
    }

    /**
     * POST /api/v1/products - Crée un nouveau produit
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody CreateProductDto createProductDto) {
        log.debug("POST /api/v1/products - Création d'un produit: {}", createProductDto.getName());
        ProductResponseDto product = productService.createProduct(createProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * PUT /api/v1/products/{id} - Met à jour un produit existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto updateProductDto) {
        log.debug("PUT /api/v1/products/{} - Mise à jour du produit", id);
        ProductResponseDto product = productService.updateProduct(id, updateProductDto);
        return ResponseEntity.ok(product);
    }

    /**
     * DELETE /api/v1/products/{id} - Supprime un produit (suppression logique)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("DELETE /api/v1/products/{} - Suppression logique du produit", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/v1/products/{id}/hard - Supprime définitivement un produit
     */
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable Long id) {
        log.debug("DELETE /api/v1/products/{}/hard - Suppression définitive du produit", id);
        productService.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/products/search - Recherche des produits par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(
            @RequestParam String name) {
        log.debug("GET /api/v1/products/search?name={} - Recherche de produits", name);
        List<ProductResponseDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/v1/products/category/{category} - Récupère les produits par catégorie
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(
            @PathVariable String category) {
        log.debug("GET /api/v1/products/category/{} - Récupération par catégorie", category);
        List<ProductResponseDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/v1/products/price-range - Récupère les produits dans une gamme de prix
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.debug("GET /api/v1/products/price-range?minPrice={}&maxPrice={}", minPrice, maxPrice);
        List<ProductResponseDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/v1/products/available - Récupère les produits disponibles (en stock)
     */
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponseDto>> getAvailableProducts() {
        log.debug("GET /api/v1/products/available - Récupération des produits disponibles");
        List<ProductResponseDto> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/v1/products/low-stock - Récupère les produits avec stock faible
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold) {
        log.debug("GET /api/v1/products/low-stock?threshold={}", threshold);
        List<ProductResponseDto> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }

    /**
     * PATCH /api/v1/products/{id}/stock/increase - Augmente le stock d'un produit
     */
    @PatchMapping("/{id}/stock/increase")
    public ResponseEntity<ProductResponseDto> increaseStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        log.debug("PATCH /api/v1/products/{}/stock/increase", id);
        Integer quantity = request.get("quantity");
        ProductResponseDto product = productService.updateStock(id, quantity, true);
        return ResponseEntity.ok(product);
    }

    /**
     * PATCH /api/v1/products/{id}/stock/decrease - Réduit le stock d'un produit
     */
    @PatchMapping("/{id}/stock/decrease")
    public ResponseEntity<ProductResponseDto> decreaseStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        log.debug("PATCH /api/v1/products/{}/stock/decrease", id);
        Integer quantity = request.get("quantity");
        ProductResponseDto product = productService.updateStock(id, quantity, false);
        return ResponseEntity.ok(product);
    }
}