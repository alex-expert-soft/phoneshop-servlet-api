<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.entity.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <p></p>
    <c:if test="${empty cart.items}">
        Cart is empty yet
    </c:if>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>

    <c:if test="${not empty errors}">
        <div class="error">
            Error occurred while placing order
        </div>
    </c:if>
    <c:if test="${not empty cart.items}">

        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
            <div>
                <table>
                    <thead>
                    <tr>
                        <td>Image</td>
                        <td class="description">
                            Description
                        </td>
                        <td class="price">
                            Price
                        </td>
                        <td>
                            Quantity
                        </td>
                    </tr>
                    </thead>
                    <c:forEach var="cartItem" items="${order.items}" varStatus="status">
                        <tr>
                            <td>
                                <img class="product-tile" src=${cartItem.product.imageUrl}>
                            </td>
                            <td>
                                    ${cartItem.product.description}
                            </td>
                            <td>
                                <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                                  currencySymbol="${cartItem.product.currency.symbol}"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${cartItem.quantity}" var="quantity"/>
                                    ${quantity}
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                            Subtotal
                        </td>
                        <td class="price">
                            <p>
                                <fmt:formatNumber value="${order.subtotal}" type="currency"
                                                  currencySymbol="${order.currency.symbol}"/>
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                            Delivery cost
                        </td>
                        <td class="price">
                            <p>
                                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                                  currencySymbol="${order.currency.symbol}"/>
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                            Total cost
                        </td>
                        <td class="price">
                            <p>
                                <fmt:formatNumber value="${order.totalCost}" type="currency"
                                                  currencySymbol="${order.currency.symbol}"/>
                            </p>
                        </td>
                    </tr>
                </table>
            </div>

            <h3>
                Your details
            </h3>
            <table>
                <tags:orderFormRow name="firstName" label="First name" placeholder="" order="${order}"
                                   errors="${errors}"/>
                <tags:orderFormRow name="lastName" label="Last name" placeholder="" order="${order}"
                                   errors="${errors}"/>
                <tags:orderFormRow name="phone" label="Phone" order="${order}" placeholder="+375 (хх) ххх-хх-хх"
                                   errors="${errors}"/>
                <tags:orderFormRow name="deliveryDate" label="Delivery date" placeholder="2021-20-07" order="${order}"
                                   errors="${errors}"/>
                <tags:orderFormRow name="deliveryAddress" label="Delivery address" placeholder=""
                                   order="${order}" errors="${errors}"/>
                <tr>
                    <td>Payment method</td>
                    <td>
                        <select name="paymentMethod">
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option>${paymentMethod.toString()}</option>
                                <c:if test="${order.paymentMethod.equals(paymentMethod)}">
                                    ${paymentMethod}
                                </c:if>
                            </c:forEach>
                        </select>
                        <c:set var="error" value="${errors['paymentMethod']}"/>
                        <c:if test="${not empty errors}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>

            <p>
                <button>
                    Place order
                </button>
            </p>
        </form>

    </c:if>

    <c:if test="${not empty recentlyViewed}">
        <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
    </c:if>
</tags:master>
