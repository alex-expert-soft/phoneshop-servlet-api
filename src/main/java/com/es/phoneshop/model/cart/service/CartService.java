package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.entity.Cart;

import javax.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpSession session);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void update(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void delete(Cart cart, Long productId);

    void clearCart(Cart cart);
}
