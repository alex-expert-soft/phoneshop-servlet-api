package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAttributes(request);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] productIds = request.getParameterValues(RequestParameter.PRODUCT_ID);
        String[] quantities = request.getParameterValues(RequestParameter.QUANTITY);

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            try {
                quantity = getQuantity(quantities[i], request);
                final Cart cart = cartService.getCart(request.getSession());
                cartService.update(cart, productId, quantity);
            } catch (ParseException | OutOfStockException ex) {
                handleError(errors, productId, ex);
            }
        }


        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "cart?message=Cart updated successfully");
        } else {
            request.setAttribute(RequestParameter.ERRORS, errors);
            doGet(request, response);
        }

    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else {
            if (((OutOfStockException) e).getStockAvailable() <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available " + ((OutOfStockException) e).getStockAvailable());
            }
        }
    }

    private void setAttributes(HttpServletRequest request) {
        request.setAttribute(RequestParameter.CART, cartService.getCart(request.getSession()));
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = getNumberFormat(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }

    protected NumberFormat getNumberFormat(Locale locale) {
        return NumberFormat.getInstance(locale);
    }

}
