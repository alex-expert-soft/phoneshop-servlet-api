package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException {
    private Long id;
    private String secureId;

    public OrderNotFoundException(Long id) {
        super("Order not found " + id);
    }

    public OrderNotFoundException(String secureId) {
        super("Order not found " + secureId);
    }

    public Long getId() {
        return id;
    }

    public String getSecureId() {
        return secureId;
    }
}
