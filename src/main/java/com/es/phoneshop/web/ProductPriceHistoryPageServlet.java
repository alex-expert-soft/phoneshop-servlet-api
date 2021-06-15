package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productInfo = request.getPathInfo().substring(1);
        Product product = null;
        Long productId;

        try {
            productId = Long.valueOf(productInfo);
            product = productDao.getProduct(productId);
        } catch (NumberFormatException ex) {
            request.setAttribute(RequestParameter.ERROR, "Number format exception");
        } catch (ProductNotFoundException ex) {
            request.setAttribute(RequestParameter.MESSAGE, "Product " + productInfo + " not found");
            response.sendError(404);
        }

        request.setAttribute(RequestParameter.PRODUCT, product);
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(request, response);
    }
}
