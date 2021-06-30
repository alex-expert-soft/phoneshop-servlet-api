package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String searchQuery, String sortField, String sortOrder);

    List<Product> searchProducts(String description,String searchType, BigDecimal minPrice, BigDecimal maxPrice);

    void save(Product product);

    void delete(Long id);

    void changePrice(Long id, BigDecimal price);
}
