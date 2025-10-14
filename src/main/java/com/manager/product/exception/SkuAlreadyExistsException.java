package com.manager.product.exception;

/**
 * Exception levée quand un SKU existe déjà
 */
public class SkuAlreadyExistsException extends RuntimeException {
    
    public SkuAlreadyExistsException(String message) {
        super(message);
    }
    
    public SkuAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}