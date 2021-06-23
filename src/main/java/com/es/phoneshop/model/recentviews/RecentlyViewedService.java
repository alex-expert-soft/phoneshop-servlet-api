package com.es.phoneshop.model.recentviews;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface RecentlyViewedService {
    List<Product> getRecentlyViewed(HttpSession session);

    void add(List<Product> recentlyViewed, Product product);
}
