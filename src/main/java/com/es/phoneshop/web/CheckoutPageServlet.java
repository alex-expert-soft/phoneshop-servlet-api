package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.entity.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.order.entity.Order;
import com.es.phoneshop.model.order.entity.PaymentMethod;
import com.es.phoneshop.model.order.service.DefaultOrderServiceImpl;
import com.es.phoneshop.model.order.service.OrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderServiceImpl.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(RequestParameter.ORDER, orderService.getOrder(cartService.getCart(request.getSession())));
        request.setAttribute(RequestParameter.PAYMENT_METHODS, orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> errors = new HashMap<>();
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        request.setAttribute(RequestParameter.PAYMENT_METHODS, orderService.getPaymentMethods());

        setRequiredStringParameter(request, RequestParameter.FIRST_NAME, errors, order::setFirstName);
        setRequiredStringParameter(request, RequestParameter.LAST_NAME, errors, order::setLastName);
        setRequiredStringParameter(request, RequestParameter.DELIVERY_ADDRESS, errors, order::setDeliveryAddress);
        setRequiredPhone(request, errors, order);
        setRequiredDeliveryDate(request, errors, order);
        setRequiredPaymentMethod(request, errors, order);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(cart);
            response.sendRedirect(request.getContextPath()
                    + "/order/overview/"
                    + order.getId());
        } else {
            request.setAttribute(RequestParameter.ERRORS, errors);
            request.setAttribute(RequestParameter.ORDER, orderService.getOrder(cartService.getCart(request.getSession())));
            request.setAttribute(RequestParameter.PAYMENT_METHOD, orderService.getPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    private void setRequiredStringParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                            Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setRequiredPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(RequestParameter.PAYMENT_METHOD);
        if (value == null || value.isEmpty()) {
            errors.put(RequestParameter.PAYMENT_METHOD, "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }

    private void setRequiredPhone(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter(RequestParameter.PHONE);

        if (value.matches("^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$")) {
            order.setPhone(value);
        } else if (value.isEmpty()) {
            errors.put(RequestParameter.PHONE, "Value is required");
        } else {
            errors.put(RequestParameter.PHONE, "Incorrect phone");
        }
    }

    private void setRequiredDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String dateString = request.getParameter(RequestParameter.DELIVERY_DATE);
        if (dateString == null || dateString.isEmpty()) {
            errors.put(RequestParameter.DELIVERY_DATE, "Value is required");
        } else {
            String[] dateArray = dateString.split("-");
            try {
                int year = Integer.parseInt(dateArray[0]);
                int month = Integer.parseInt(dateArray[1]);
                int day = Integer.parseInt(dateArray[2]);
                LocalDate date = LocalDate.of(year, month, day);
                if (date.compareTo(LocalDate.now()) >= 0) {
                    order.setDeliveryDate(date);
                } else {
                    errors.put(RequestParameter.DELIVERY_DATE, "Date must be greater than today");
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateTimeException e) {
                errors.put(RequestParameter.DELIVERY_DATE, "Incorrect date");
            }
        }
    }
}
