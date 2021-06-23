package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.recentviews.DefaultRecentlyViewedService;
import com.es.phoneshop.model.recentviews.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sortParam = Optional.ofNullable(request.getParameter(RequestParameter.SORT)).orElse(" ");
        String orderParam = Optional.ofNullable(request.getParameter(RequestParameter.ORDER)).orElse(" ");
        String searchParam = Optional.ofNullable(request.getParameter(RequestParameter.QUERY)).orElse(" ");
        request.setAttribute(RequestParameter.PRODUCTS, productDao.findProducts(searchParam, sortParam, orderParam));
        request.setAttribute(RequestParameter.RECENTLY_VIEWED, recentlyViewedService.getRecentlyViewed(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

}
