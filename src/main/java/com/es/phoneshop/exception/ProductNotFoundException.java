package com.es.phoneshop.exception;

public class ProductNotFoundException extends RuntimeException {
    private Long id;

    public ProductNotFoundException(Long id) {
        super("Product not found" + id);
    }

    public Long getId() {
        return id;
    }
}
