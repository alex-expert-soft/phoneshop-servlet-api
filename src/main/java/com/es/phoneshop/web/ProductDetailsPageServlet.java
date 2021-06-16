package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentviews.DefaultRecentViewService;
import com.es.phoneshop.model.recentviews.RecentView;
import com.es.phoneshop.model.recentviews.RecentViewService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentViewService recentViewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentViewService = DefaultRecentViewService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = null;
        Long productId;
        RecentView recentView = recentViewService.getRecentView(request.getSession());
        String productInfo = request.getPathInfo().substring(1);

        try {
            productId = Long.valueOf(productInfo);
            product = productDao.getProduct(productId);
        } catch (NumberFormatException ex) {
            request.setAttribute(RequestParameter.ERROR, "Number format exception");
        } catch (ProductNotFoundException ex) {
            request.setAttribute(RequestParameter.MESSAGE, "Product " + productInfo + " not found");
            response.sendError(404);
        }

        recentViewService.add(recentView, product);
        request.setAttribute(RequestParameter.PRODUCT, product);
        request.setAttribute(RequestParameter.RECENT_VIEWS, recentView.getRecentlyViewed());
        request.setAttribute(RequestParameter.CART, cartService.getCart(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String productInfo = request.getPathInfo().substring(1);
        Long productId = Long.valueOf(productInfo);
        String quantityStr = request.getParameter(RequestParameter.QUANTITY);
        int quantity;

        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityStr).intValue();
        } catch (ParseException ex) {
            request.setAttribute(RequestParameter.ERROR, "Number format exception");
            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request.getSession());
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException ex) {
            request.setAttribute(RequestParameter.ERROR, "Out of stock, available " + ex.getStockAvailable());
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }


}
