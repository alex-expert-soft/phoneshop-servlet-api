package com.es.phoneshop.model.cart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart implements Serializable {
    private List<CartItem> items;

    private int totalQuantity;

    public Cart() {
        this.items = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Cart[" + items + "]";
    }
}
