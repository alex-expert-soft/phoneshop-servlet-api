package com.es.phoneshop.model.order.service;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.cart.entity.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.order.entity.Order;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.entity.Product;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultOrderServiceImplTest {
    private OrderService orderService;
    private CartService cartService;
    private ProductDao productDao;

    @Before
    public void setup() {
        orderService = DefaultOrderServiceImpl.getInstance();
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void placeOrderSuccess() {
        final Order order = new Order();
        orderService.placeOrder(order);

        assertNotNull(orderService.getOrder(order.getId()));
    }

    @Test
    public void placeOrderNullOrder() {
        assertThrows(NullPointerException.class,
                () -> orderService.placeOrder(null));
    }

    @Test
    public void getOrderByIdSuccess() {
        final Order order = new Order();
        final Long id = 1L;
        order.setId(id);
        orderService.placeOrder(order);

        assertEquals(id, orderService.getOrder(id).getId());
    }

    @Test
    public void getOrderByIdNotFound() {
        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrder(100L));
    }

    @Test
    public void getOrderNullArguments() {
        assertThrows(NullPointerException.class,
                () -> orderService.getOrder((Long) null));
        assertThrows(NullPointerException.class,
                () -> orderService.getOrder((Cart) null));
        assertThrows(NullPointerException.class,
                () -> orderService.getOrderBySecureId(null));
    }

    @Test
    public void getOrderBySecureIdSuccess() {
        final Order order = new Order();
        orderService.placeOrder(order);
        final String secureId = order.getSecureId();

        assertEquals(secureId, orderService.getOrderBySecureId(secureId).getSecureId());
    }

    @Test
    public void getOrderBySecureIdNotFound() {
        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderBySecureId(UUID.randomUUID().toString()));
    }

    @SneakyThrows
    @Test
    public void getOrderByCartSuccess() {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        cartService.add(cart, product.getId(), 1);

        assertNotNull(orderService.getOrder(cart));
    }

    @Test
    public void getOrderByCartEmptyCart() {
        assertThrows(NullPointerException.class,
                () -> orderService.getOrder(new Cart()));
    }

}