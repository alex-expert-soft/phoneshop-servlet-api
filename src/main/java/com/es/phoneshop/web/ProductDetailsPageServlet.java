package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.recentviews.DefaultRecentlyViewedService;
import com.es.phoneshop.model.recentviews.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Long productId;
        String productInfo = request.getPathInfo().substring(1);

        try {
            productId = Long.valueOf(productInfo);
            Product product = productDao.getProduct(productId);
            recentlyViewedService.add(recentlyViewedService.getRecentlyViewed(request.getSession()), product);
            request.setAttribute(RequestParameter.PRODUCT, product);
        } catch (NumberFormatException ex) {
            request.setAttribute(RequestParameter.ERROR, "Number format exception");
        } catch (ProductNotFoundException ex) {
            request.setAttribute(RequestParameter.MESSAGE, "Product " + productInfo + " not found");
            response.sendError(404);
            return;
        }


        request.setAttribute(RequestParameter.RECENTLY_VIEWED, recentlyViewedService.getRecentlyViewed(request.getSession()));
        request.setAttribute(RequestParameter.CART, cartService.getCart(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);

    }



}
