package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
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
import java.util.Optional;

public class AddCartItemServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quantityParam = Optional.ofNullable(request.getParameter(RequestParameter.QUANTITY))
                .orElse(" ");
        String productIdParam = Optional.ofNullable(request.getParameter(RequestParameter.PRODUCT_ID))
                .orElse(" ");
        String redirectPath = Optional.ofNullable(request.getParameter(RequestParameter.REDIRECT))
                .orElse(" ");

        if (redirectPath.equals(RequestParameter.PDP)) {
            redirectPath = RequestParameter.PDP_PAGE + Long.valueOf(productIdParam);
        } else if (redirectPath.equals(RequestParameter.PLP)) {
            redirectPath = RequestParameter.PLP_PAGE;
        }

        StringBuilder path = new StringBuilder(request.getContextPath())
                .append(redirectPath)
                .append("?" + RequestParameter.PRODUCT_ID + "=").append(productIdParam)
                .append("&" + RequestParameter.QUANTITY + "=").append(quantityParam);

        try {
            final int quantity = getParsedQuantity(quantityParam, request.getLocale());
            final Long productId = Long.valueOf(productIdParam);
            cartService.add(cartService.getCart(request.getSession()), productId, quantity);
        } catch (OutOfStockException ex) {
            path.append("&" + RequestParameter.ERROR + "=Out of stock");
            response.sendRedirect(path.toString());
            return;
        } catch (ParseException | IllegalArgumentException ex) {
            path.append("&" + RequestParameter.ERROR + "=Incorrect input");
            response.sendRedirect(path.toString());
            return;
        }

        path.append("&" + RequestParameter.MESSAGE + "=Added to cart successfully");
        response.sendRedirect(path.toString());
    }

    private int getParsedQuantity(String quantity, Locale locale) throws ParseException {
        if (quantity.matches("\\d+")) {
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            return numberFormat.parse(quantity).intValue();
        } else {
            throw new ParseException(quantity, 0);
        }
    }
}
