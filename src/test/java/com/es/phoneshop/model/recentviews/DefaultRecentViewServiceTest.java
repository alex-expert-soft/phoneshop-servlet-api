package com.es.phoneshop.model.recentviews;

import com.es.phoneshop.model.product.entity.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentViewServiceTest {
    private static final String RECENTLY_VIEWED_SERVICE_ATTRIBUTE = DefaultRecentlyViewedService.class.getName() + ".recent_views";

    private RecentlyViewedService recentlyViewedService;
    private List<Product> recentlyViewed;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Before
    public void init() {
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
        recentlyViewed = new ArrayList<>();
        when(session.getAttribute(RECENTLY_VIEWED_SERVICE_ATTRIBUTE)).thenReturn(recentlyViewed);
    }

    @Test
    public void addProductToRecentlyViewedSuccess() {
        final Currency usd = Currency.getInstance("USD");
        final Product prod1 = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        final Product prod2 = new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        final Product prod3 = new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");

        recentlyViewedService.add(recentlyViewed, prod1);
        recentlyViewedService.add(recentlyViewed, prod2);
        recentlyViewedService.add(recentlyViewed, prod3);

        assertEquals(3, recentlyViewedService.getRecentlyViewed(session).size());
    }

    @Test
    public void addProductToRecentlyViewedReplaceFirst() {
        final Currency usd = Currency.getInstance("USD");
        final Product prod1 = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        final Product prod2 = new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        final Product prod3 = new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        final Product prod4 = new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");

        recentlyViewedService.add(recentlyViewed, prod1);
        recentlyViewedService.add(recentlyViewed, prod2);
        recentlyViewedService.add(recentlyViewed, prod3);
        assertTrue(recentlyViewedService.getRecentlyViewed(session).contains(prod1));

        recentlyViewedService.add(recentlyViewed, prod4);
        assertEquals(3, recentlyViewedService.getRecentlyViewed(session).size());
        assertFalse(recentlyViewedService.getRecentlyViewed(session).contains(prod1));
    }

    @Test
    public void addProductToRecentlyViewedNullArguments() {
        final Currency usd = Currency.getInstance("USD");
        final Product prod = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        assertThrows(NullPointerException.class,
                () -> recentlyViewedService.add(null, null));
        assertThrows(NullPointerException.class,
                () -> recentlyViewedService.add(null, prod));
    }

    @Test
    public void getRecentlyViewedEmptyList() {
       assertTrue(recentlyViewedService.getRecentlyViewed(session).isEmpty());
    }
}