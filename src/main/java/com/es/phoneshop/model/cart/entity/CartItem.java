package com.es.phoneshop.model.cart.entity;

import com.es.phoneshop.model.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class CartItem implements Serializable,Cloneable {
    private Product product;
    private int quantity;

    @Override
    public String toString() {
        return product.getCode() + ", " + quantity;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

