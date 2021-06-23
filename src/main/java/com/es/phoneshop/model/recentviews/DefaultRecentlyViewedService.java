package com.es.phoneshop.model.recentviews;

import com.es.phoneshop.model.product.Product;
import lombok.NonNull;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultRecentlyViewedService implements RecentlyViewedService {
    private static final String RECENT_VIEW_SESSION_ATTRIBUTE = DefaultRecentlyViewedService.class.getName() + ".recent_views";
    private static volatile DefaultRecentlyViewedService instance;
    private static final int NUMBER_OF_PRODUCTS = 3;

    private DefaultRecentlyViewedService() {

    }

    public static DefaultRecentlyViewedService getInstance() {
        if (instance == null) {
            synchronized (DefaultRecentlyViewedService.class) {
                if (instance == null) {
                    instance = new DefaultRecentlyViewedService();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Product> getRecentlyViewed(@NonNull final HttpSession session) {
        synchronized (session) {
            List<Product> products = (ArrayList<Product>) session.getAttribute(RECENT_VIEW_SESSION_ATTRIBUTE);
            if (products == null) {
                session.setAttribute(RECENT_VIEW_SESSION_ATTRIBUTE, products = new ArrayList<>());
            }
            return products;
        }
    }

    @Override
    public void add(@NonNull final List<Product> recentlyViewed, @NonNull final Product product) {
        synchronized (recentlyViewed) {
            recentlyViewed.remove(product);

            recentlyViewed.add(product);
            Collections.rotate(recentlyViewed, 1);

            if (recentlyViewed.size() == NUMBER_OF_PRODUCTS + 1) {
                recentlyViewed.remove(NUMBER_OF_PRODUCTS);
            }
        }
    }
}
