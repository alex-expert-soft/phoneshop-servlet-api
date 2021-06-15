package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import lombok.NonNull;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;
    public static volatile DefaultCartService instance;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized Cart getCart(@NonNull final HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(@NonNull final Cart cart, @NonNull final Long productId, final int quantity) throws OutOfStockException {

        final Optional<CartItem> cartItemOptional = findCartItemOptional(cart, productId, quantity);

        final Product product = productDao.getProduct(productId);
        final int currentQuantity = cartItemOptional.map(CartItem::getQuantity).orElse(0);

        if (product.getStock() < quantity + currentQuantity) {
            throw new OutOfStockException(product, quantity + currentQuantity, product.getStock());
        }

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(currentQuantity + quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(@NonNull final Cart cart, @NonNull final Long productId, final int quantity) throws OutOfStockException {

        final Optional<CartItem> cartItemOptional = findCartItemOptional(cart, productId, quantity);

        final Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        recalculateCart(cart);
    }

    @Override
    public void delete(@NonNull final Cart cart, @NonNull final Long productId) {
        cart.getItems().removeIf(item ->
                productId.equals(item.getProduct().getId())
        );
    }

    private void recalculateCart(@NonNull final Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(Integer::intValue).sum());
    }

    private Optional<CartItem> findCartItemOptional(@NonNull final Cart cart, @NonNull final Long productId, final int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }
        if (productId < 1) {
            throw new IllegalArgumentException("Incorrect quantity of products");
        }

        final Product product = productDao.getProduct(productId);

        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findAny();
    }
}
