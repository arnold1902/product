package com.manager.product.service;

import com.manager.product.dto.CreateProductDto;
import com.manager.product.dto.ProductResponseDto;
import com.manager.product.dto.UpdateProductDto;
import com.manager.product.entity.Product;
import com.manager.product.exception.ProductNotFoundException;
import com.manager.product.exception.SkuAlreadyExistsException;
import com.manager.product.mapper.ProductMapper;
import com.manager.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service pour la gestion des produits
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final String NOT_FOUND_MESSAGE = "Produit non trouvé avec l'ID: ";

    /**
     * Récupère tous les produits avec pagination
     */
    @Cacheable(value = "products-page", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        log.debug("Récupération de tous les produits avec pagination: {}", pageable);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponseDto> productDtos = productPage.getContent().stream()
                .map(productMapper::toResponseDto)
                .toList();
        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    /**
     * Récupère tous les produits actifs
     */
    public List<ProductResponseDto> getActiveProducts() {
        log.debug("Récupération de tous les produits actifs");
        List<Product> products = productRepository.findByActiveTrue();
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    /**
     * Récupère un produit par son ID
     */
    @Cacheable(value = "product", key = "#id")
    public ProductResponseDto getProductById(Long id) {
        log.debug("Récupération du produit avec l'ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND_MESSAGE + id));
        return productMapper.toResponseDto(product);
    }

    /**
     * Récupère un produit par son SKU
     */
    public ProductResponseDto getProductBySku(String sku) {
        log.debug("Récupération du produit avec le SKU: {}", sku);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Produit non trouvé avec le SKU: " + sku));
        return productMapper.toResponseDto(product);
    }

    /**
     * Crée un nouveau produit
     */
    @CachePut(value = "products", key = "#result.id")
    @Transactional
    public ProductResponseDto createProduct(CreateProductDto createProductDto) {
        log.debug("Création d'un nouveau produit: {}", createProductDto.getName());
        
        // Vérifier l'unicité du SKU si fourni
        if (createProductDto.getSku() != null && productRepository.existsBySku(createProductDto.getSku())) {
            throw new SkuAlreadyExistsException("Un produit avec le SKU " + createProductDto.getSku() + " existe déjà");
        }

        Product product = productMapper.toEntity(createProductDto);
        Product savedProduct = productRepository.save(product);
        log.info("Produit créé avec succès avec l'ID: {}", savedProduct.getId());
        return productMapper.toResponseDto(savedProduct);
    }

    /**
     * Met à jour un produit existant
     */
    @CacheEvict(value = "products-page", allEntries = true)
    @CachePut(value = "product", key = "#id")
    @Transactional
    public ProductResponseDto updateProduct(Long id, UpdateProductDto updateProductDto) {
        log.debug("Mise à jour du produit avec l'ID: {}", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND_MESSAGE + id));

        // Vérifier l'unicité du SKU si modifié
        if (updateProductDto.getSku() != null && 
            productRepository.existsBySkuAndIdNot(updateProductDto.getSku(), id)) {
            throw new SkuAlreadyExistsException("Un autre produit avec le SKU " + updateProductDto.getSku() + " existe déjà");
        }

        productMapper.updateEntityFromDto(updateProductDto, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Produit mis à jour avec succès avec l'ID: {}", updatedProduct.getId());
        return productMapper.toResponseDto(updatedProduct);
    }

    /**
     * Supprime un produit (suppression logique)
     */
    @CacheEvict(cacheNames = {"products-page", "product"}, allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {
        log.debug("Suppression du produit avec l'ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND_MESSAGE + id));
        
        product.setActive(false);
        productRepository.save(product);
        log.info("Produit supprimé (logiquement) avec succès avec l'ID: {}", id);
    }

    /**
     * Supprime définitivement un produit
     */
    @Transactional
    public void hardDeleteProduct(Long id) {
        log.debug("Suppression définitive du produit avec l'ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(NOT_FOUND_MESSAGE + id);
        }
        
        productRepository.deleteById(id);
        log.info("Produit supprimé définitivement avec succès avec l'ID: {}", id);
    }

    /**
     * Recherche des produits par nom
     */
    public List<ProductResponseDto> searchProductsByName(String name) {
        log.debug("Recherche de produits par nom: {}", name);
        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    /**
     * Récupère les produits par catégorie
     */
    public List<ProductResponseDto> getProductsByCategory(String category) {
        log.debug("Récupération des produits par catégorie: {}", category);
        List<Product> products = productRepository.findByCategoryIgnoreCaseAndActiveTrue(category);
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    /**
     * Récupère les produits dans une gamme de prix
     */
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Récupération des produits dans la gamme de prix: {} - {}", minPrice, maxPrice);
        List<Product> products = productRepository.findByPriceBetweenAndActiveTrue(minPrice, maxPrice);
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    /**
     * Récupère les produits disponibles (en stock)
     */
    public List<ProductResponseDto> getAvailableProducts() {
        log.debug("Récupération des produits disponibles");
        List<Product> products = productRepository.findAvailableProducts();
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    /**
     * Récupère les produits avec stock faible
     */
    public List<ProductResponseDto> getLowStockProducts(Integer threshold) {
        log.debug("Récupération des produits avec stock faible (seuil: {})", threshold);
        List<Product> products = productRepository.findProductsWithLowStock(threshold != null ? threshold : 10);
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    /**
     * Met à jour le stock d'un produit
     */
    @Transactional
    public ProductResponseDto updateStock(Long id, Integer quantity, boolean isIncrease) {
        log.debug("Mise à jour du stock pour le produit ID: {}, quantité: {}, augmentation: {}", 
                 id, quantity, isIncrease);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND_MESSAGE + id));
        
        if (isIncrease) {
            product.increaseStock(quantity);
        } else {
            product.reduceStock(quantity);
        }
        
        Product updatedProduct = productRepository.save(product);
        log.info("Stock mis à jour pour le produit ID: {}, nouveau stock: {}", 
                id, updatedProduct.getQuantityInStock());
        return productMapper.toResponseDto(updatedProduct);
    }
}