<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" class="com.es.phoneshop.model.cart.entity.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p></p>
    <c:if test="${empty cart.items}">
        Cart is empty yet
    </c:if>
    <c:if test="${not empty cart.items}">
        <c:if test="${not empty param.message}">
            <div class="success">
                    ${param.message}
            </div>
        </c:if>

        <c:if test="${not empty errors}">
            <div class="error">
                There were errors updating cart
            </div>
        </c:if>

        <form method="post" action="${pageContext.servletContext.contextPath}/cart">
            <table>
                <thead>
                <tr>
                    <td>Image</td>
                    <td class="description">
                        Description
                    </td>
                    <td>
                        Quantity
                    </td>
                    <td class="price">
                        Price
                    </td>
                    <td></td>
                </tr>
                </thead>
                <c:forEach var="item" items="${cart.items}" varStatus="status">
                    <tr>
                        <td>
                            <img class="product-tile"
                                 src="${item.product.imageUrl}">
                        </td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                    ${item.product.description}
                            </a>
                        </td>
                        <td>
                            <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                            <c:set var="error" value="${errors[item.product.id]}"/>
                            <input class="quantity" type="text" name="quantity"
                                   value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}">
                            <c:if test="${not empty error}">
                                <div class="error">
                                        ${error}
                                </div>
                            </c:if>
                            <input type="hidden" name="productId" value="${item.product.id}">
                        </td>
                        <td class="price">
                            <a href="${pageContext.servletContext.contextPath}/products/price-history/${item.product.id}">
                                <fmt:formatNumber value="${item.product.price}" type="currency"
                                                  currencySymbol="${item.product.currency.symbol}"/>
                            </a>
                        </td>
                        <td>
                            <button form="deleteCartItem"
                                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td></td>
                    <td></td>
                    <td>Total quantity:</td>
                    <td class="quantity">
                            ${cart.totalQuantity}
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td>Total cost:</td>
                    <td class="price">
                        <p>
                            <fmt:formatNumber value="${cart.totalCost}" type="currency"
                                              currencySymbol="${cart.currency.symbol}"/>
                        </p>
                    </td>
                </tr>
            </table>
            <p>
                <button>Update</button>
            </p>
        </form>

        <form action="${pageContext.servletContext.contextPath}/checkout">
            <button>
                Checkout
            </button>
        </form>

    </c:if>
    <form id="deleteCartItem" method="post"/>

    <c:if test="${not empty recentlyViewed}">
        <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
    </c:if>
</tags:master>