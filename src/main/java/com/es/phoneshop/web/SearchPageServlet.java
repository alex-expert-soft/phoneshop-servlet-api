package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.entity.SearchType;
import com.es.phoneshop.model.recentviews.DefaultRecentlyViewedService;
import com.es.phoneshop.model.recentviews.RecentlyViewedService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class SearchPageServlet extends HttpServlet {
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
        request.setAttribute(RequestParameter.SEARCH_TYPES, Arrays.asList(SearchType.values()));
        String descriptionParam = Optional.ofNullable(request.getParameter(RequestParameter.DESCRIPTION)).orElse(" ");
        String searchTypeParam = Optional.ofNullable(request.getParameter(RequestParameter.CURRENT_SEARCH_TYPE)).orElse("All words");

        String minPriceParam = request.getParameter(RequestParameter.MIN_PRICE);
        String maxPriceParam = request.getParameter(RequestParameter.MAX_PRICE);

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        try {
            if (minPriceParam != null) {
                minPrice = getParsedPrice(minPriceParam, request.getLocale());
            }
        } catch (ParseException | IllegalArgumentException ex) {
            request.setAttribute(RequestParameter.ERROR_MIN_PRICE, "Not a number");
        }

        try {
            if (maxPriceParam != null) {
                maxPrice = getParsedPrice(maxPriceParam, request.getLocale());
            }
        } catch (ParseException | IllegalArgumentException ex) {
            request.setAttribute(RequestParameter.ERROR_MAX_PRICE, "Not a number");
        }

        request.setAttribute(RequestParameter.PRODUCTS, productDao.searchProducts(descriptionParam, searchTypeParam, minPrice, maxPrice));
        request.setAttribute(RequestParameter.RECENTLY_VIEWED, recentlyViewedService.getRecentlyViewed(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/search.jsp").forward(request, response);
    }

    private BigDecimal getParsedPrice(String quantity, Locale locale) throws ParseException {
        if (quantity.matches("\\d+")) {
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            return BigDecimal.valueOf(numberFormat.parse(quantity).longValue());
        } else {
            throw new ParseException(quantity, 0);
        }
    }

}
