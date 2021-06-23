package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException {
    private Long id;
    private String secureId;

    public OrderNotFoundException(Long id) {
        this.id = id;
    }

    public OrderNotFoundException(String secureId) {
        this.secureId = secureId;
    }

    public Long getId() {
        return id;
    }

    public String getSecureId() {
        return secureId;
    }
}
