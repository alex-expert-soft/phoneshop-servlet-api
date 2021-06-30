package com.es.phoneshop.model.product.entity;

public enum SearchType {
    All_WORDS("All words"), ANY_WORD("Any word");

    private final String searchType;

    SearchType(String searchType) {
        this.searchType = searchType;
    }

    public static SearchType fromString(String text) {
        for (SearchType type : SearchType.values()) {
            if (type.searchType.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return searchType;
    }
}
