package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.entity.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.recentviews.RecentlyViewedService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private RecentlyViewedService recentlyViewedService;

    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(productDao.getProduct(1L)).thenReturn(new Product());
        when(request.getPathInfo()).thenReturn("/1");
        when(recentlyViewedService.getRecentlyViewed(request.getSession())).thenReturn(new ArrayList<>());
        when(cartService.getCart(request.getSession())).thenReturn(cart);
    }

    @SneakyThrows
    @Test
    public void testDoGet() {
        servlet.doGet(request, response);

        verify(request).setAttribute(RequestParameter.PRODUCT, productDao.getProduct(1L));
        verify(request).setAttribute(RequestParameter.CART, cart);
        verify(requestDispatcher).forward(request, response);
    }

    @SneakyThrows
    @Test
    public void testDoGetIncorrectNumber() {
        when(request.getPathInfo()).thenReturn("/s");
        servlet.doGet(request, response);

        verify(request).setAttribute(RequestParameter.ERROR, "Number format exception");
    }

    @SneakyThrows
    @Test
    public void testDoGetNotFound() {
        when(productDao.getProduct(1L)).thenReturn(null);
        servlet.doGet(request, response);

        verify(request).setAttribute(RequestParameter.ERROR, eq(any()));
    }

}