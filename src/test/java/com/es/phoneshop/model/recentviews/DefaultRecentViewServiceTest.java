package com.es.phoneshop.model.recentviews;

import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultRecentViewServiceTest {
    private RecentlyViewedService recentlyViewedService;

    @Before
    public void init() {
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Test
    public void addProductToRecentViewFirst() {
        final Currency usd = Currency.getInstance("USD");
        final Product prod = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        //TODO dodelat
    }

    @Test
    public void addProductToRecentViewRemoveOldProduct() {
        final Currency usd = Currency.getInstance("USD");

        final Product prod1 = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        final Product prod2 = new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        final Product prod3 = new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        final Product prod4 = new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");


    }

    @Test
    public void addProductToRecentViewNullArguments() {
        final Currency usd = Currency.getInstance("USD");
        final Product prod = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        assertThrows(NullPointerException.class,
                () -> recentlyViewedService.add(null, null));
        assertThrows(NullPointerException.class,
                () -> recentlyViewedService.add(null, prod));
    }

}