package com.es.phoneshop.model.order.service;

import com.es.phoneshop.model.cart.entity.Cart;
import com.es.phoneshop.model.cart.entity.CartItem;
import com.es.phoneshop.model.order.dao.ArrayListOrderDao;
import com.es.phoneshop.model.order.dao.OrderDao;
import com.es.phoneshop.model.order.entity.Order;
import com.es.phoneshop.model.order.entity.PaymentMethod;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderServiceImpl implements OrderService {
    private OrderDao orderDao;
    private static volatile DefaultOrderServiceImpl instance;

    private DefaultOrderServiceImpl() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static DefaultOrderServiceImpl getInstance() {
        if (instance == null) {
            synchronized (DefaultOrderServiceImpl.class) {
                if (instance == null) {
                    instance = new DefaultOrderServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getOrder(@NonNull final Long id) {
        return orderDao.getOrder(id);
    }

    @Override
    public Order getOrder(@NonNull final Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return ((CartItem) item.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));

        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(@NonNull final Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public Order getOrderBySecureId(@NonNull final String secureId) {
        return orderDao.getOrderBySecureId(secureId);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }
}
