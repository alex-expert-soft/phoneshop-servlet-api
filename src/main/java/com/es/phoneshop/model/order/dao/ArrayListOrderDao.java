package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.entity.Order;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private List<Order> orders;
    private long orderId;

    private static volatile ArrayListOrderDao instance;
    private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
    }

    public static ArrayListOrderDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListOrderDao.class) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException{
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            return orders.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(()->new OrderNotFoundException(id));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            return orders.stream()
                    .filter(product -> secureId.equals(product.getSecureId()))
                    .findAny()
                    .orElseThrow(()->new OrderNotFoundException(secureId));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(@NonNull final Order order) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            if (order.getId() != null) {
                Optional<Order> orderOpt = orders.stream()
                        .filter(prod -> prod.getId().equals(order.getId()))
                        .findAny();

                orderOpt.ifPresent(orders::remove);
            } else {
                order.setId(++orderId);
            }
            orders.add(order);
        } finally {
            writeLock.unlock();
        }
    }
}
