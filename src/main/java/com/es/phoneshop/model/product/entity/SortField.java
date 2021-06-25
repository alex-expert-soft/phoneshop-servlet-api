package com.es.phoneshop.model.product.entity;

public enum SortField {
    DESCRIPTION("description"), PRICE("price");

    private final String sortField;

    private SortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean equals(String fieldValue) {
        return sortField.equals(fieldValue);
    }
}
