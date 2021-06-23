package com.es.phoneshop.model.sortenum;

public enum SortOrder {
    ASC("asc"), DESC("desc");

    private final String sortOrder;

    private SortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean equals(String orderValue) {
        return sortOrder.equals(orderValue);
    }
}
