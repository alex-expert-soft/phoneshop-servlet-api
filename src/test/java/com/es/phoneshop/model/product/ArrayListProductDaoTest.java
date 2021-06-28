package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.entity.Product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testSaveProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        assertTrue(productDao.findProducts("", "description", "asc").contains(product));
    }

    @Test
    public void testSaveProductWithId() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("nok3310", "Nokia3310", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        final long id = product.getId();
        Product updProduct = new Product(id, "siemens75", "Siemens SXG75", new BigDecimal(200), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(updProduct);

        assertFalse(productDao.findProducts("nokia", "description", "asc").contains(product));
        assertTrue(productDao.findProducts("siemens", "description", "asc").contains(updProduct));
    }

    @Test
    public void testGetProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        final long id = product.getId();
        assertEquals(productDao.getProduct(id), product);
    }

    @Test
    public void testDeleteAndGetProductNull() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        final long id = product.getId();
        assertEquals(productDao.getProduct(id), product);

        productDao.delete(id);
        assertThrows(ProductNotFoundException.class,
                () -> productDao.getProduct(id));
    }

    @Test
    public void testFindProducts() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        assertTrue(productDao.findProducts("siemens", "description", "asc").contains(product));
    }

    @Test
    public void testFindProductsNoResultsWithThisSearchQuery() {
        assertTrue(productDao.findProducts("nokia", "description", "asc").isEmpty());
    }

    @Test
    public void testFindProductsZeroStock() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        final long id = product.getId();

        assertEquals(productDao.getProduct(id), product);
        assertFalse(productDao.findProducts("siemens", "description", "asc").contains(product));
    }

    @Test
    public void testChangePriceSuccess() {
        Currency usd = Currency.getInstance("USD");
        final BigDecimal currentPrice = BigDecimal.valueOf(150);
        final BigDecimal newPrice = BigDecimal.valueOf(200);
        Product product = new Product("simsxg75", "Siemens SXG75", currentPrice, usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        final long id = product.getId();

        productDao.changePrice(id, newPrice);
        assertEquals(newPrice, product.getPrice());
    }

    @Test
    public void testChangePriceNullArguments() {
        assertThrows(NullPointerException.class,
                ()->productDao.changePrice(null,null));
        assertThrows(NullPointerException.class,
                ()->productDao.changePrice(1L,null));
        assertThrows(NullPointerException.class,
                ()->productDao.changePrice(null,new BigDecimal(10)));
    }
}
