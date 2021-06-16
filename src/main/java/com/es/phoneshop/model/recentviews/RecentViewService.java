package com.es.phoneshop.model.recentviews;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;

public interface RecentViewService {
    RecentView getRecentView(HttpSession session);

    void add(RecentView recentView, Product product);
}
