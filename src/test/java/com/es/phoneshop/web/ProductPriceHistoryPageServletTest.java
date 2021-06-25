package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.entity.Product;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductPriceHistoryPageServlet servlet = new ProductPriceHistoryPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(productDao.getProduct(any())).thenReturn(new Product());

    }

    @SneakyThrows
    @Test
    public void testDoGet() {
        when(request.getPathInfo()).thenReturn("/1");
        servlet.doGet(request, response);

        verify(productDao).getProduct(1L);
        verify(request).setAttribute(RequestParameter.PRODUCT, productDao.getProduct(1L));
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
        when(request.getPathInfo()).thenReturn("/1");
        when(productDao.getProduct(1L)).thenReturn(null);
        servlet.doGet(request, response);

        verify(request).setAttribute(RequestParameter.ERROR, eq(any()));
    }
}