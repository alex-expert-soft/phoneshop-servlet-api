package com.es.phoneshop.model.cart.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class Cart implements Serializable {
    private List<CartItem> items;

    private int totalQuantity;
    private BigDecimal totalCost;
    private final Currency currency = Currency.getInstance(Locale.US);

    public Cart() {
        this.items = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Cart[" + items + "]";
    }
}
