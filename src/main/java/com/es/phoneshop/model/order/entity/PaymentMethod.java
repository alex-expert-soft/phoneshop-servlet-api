package com.es.phoneshop.model.order.entity;

public enum PaymentMethod {
    CASH("Cash"), CREDIT_CARD("Credit cart");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static PaymentMethod fromString(String text) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.paymentMethod.equalsIgnoreCase(text)) {
                return method;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return paymentMethod;
    }
}
