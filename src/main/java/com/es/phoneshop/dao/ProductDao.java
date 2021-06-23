package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String searchQuery, String sortField, String sortOrder);

    void save(Product product);

    void delete(Long id);

    void changePrice(Long id, BigDecimal price);
}
