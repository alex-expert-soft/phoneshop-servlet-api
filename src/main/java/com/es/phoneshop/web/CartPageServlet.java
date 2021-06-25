package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.entity.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.recentviews.DefaultRecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;
    private DefaultRecentlyViewedService recentViewService;
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        recentViewService = DefaultRecentlyViewedService.getInstance();
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute(RequestParameter.CART, cartService.getCart(session));
        request.setAttribute(RequestParameter.RECENTLY_VIEWED, recentViewService.getRecentlyViewed(session));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] productIds = request.getParameterValues(RequestParameter.PRODUCT_ID);
        String[] quantities = request.getParameterValues(RequestParameter.QUANTITY);

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            try {
                final int quantity = getParsedQuantity(quantities[i], request.getLocale());
                final Cart cart = cartService.getCart(request.getSession());
                final Long productId = Long.valueOf(productIds[i]);
                cartService.update(cart, productId, quantity);
            } catch (ParseException | IllegalArgumentException ex) {
                errors.put(Long.valueOf(productIds[i]), "Incorrect input");
            } catch (OutOfStockException ex) {
                errors.put(Long.valueOf(productIds[i]), "Out of stock");
            }
        }


        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute(RequestParameter.ERRORS, errors);
            doGet(request, response);
        }

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
