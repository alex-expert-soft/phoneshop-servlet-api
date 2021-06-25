package com.es.phoneshop.model.order.service;

import com.es.phoneshop.model.cart.entity.Cart;
import com.es.phoneshop.model.order.entity.Order;
import com.es.phoneshop.model.order.entity.PaymentMethod;

import java.util.List;

public interface OrderService {

    Order getOrder(Long id);

    Order getOrder(Cart cart);

    List<PaymentMethod> getPaymentMethods();

    void placeOrder(Order order);

    Order getOrderBySecureId(String secureOrderId);
}
