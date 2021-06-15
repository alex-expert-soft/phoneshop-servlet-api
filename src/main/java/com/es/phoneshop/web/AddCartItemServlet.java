package com.es.phoneshop.web;

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
import java.util.Locale;

public class AddCartItemServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String productInfo = request.getPathInfo().substring(1);
        Long productId;
        String[] quantities = request.getParameterValues(RequestParameter.QUANTITY);

        try {
            productId = Long.valueOf(productInfo);
            int quantity = getQuantity(quantities[productId.intValue()], request);
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, productId, quantity);
        } catch (NumberFormatException | ParseException ex) {
            request.setAttribute(RequestParameter.ERROR, "Number format exception");
            doGet(request, response);
            return;
        } catch (OutOfStockException ex) {
            request.setAttribute(RequestParameter.ERROR, "Out of stock, available " + ex.getStockAvailable());
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products?message=Cart item added successfully");
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = getNumberFormat(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }

    protected NumberFormat getNumberFormat(Locale locale) {
        return NumberFormat.getInstance(locale);
    }
}
