<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div>
                ${param.message}
        </div>
    </c:if>
    <form method="post">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td>
                    Quantity
                </td>
                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile"
                             src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}"
                            ${item.product.description}</td>
                    <td>
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <input name="quantity"
                               value="${not empty error ? paramValues['quantity'][status.index] : quantity}">
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
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}}">
                            Delete
                        </button>
                    </td>
                </tr>
                <c:set var="totalCost" value="${totalCost+item.product.price*item.quantity}"/>
            </c:forEach>
            <td/>
            <td>Total price:</td>
            <td/>
            <td>${totalCost}</td>
        </table>
        <p>
            <button>Update</button>
        </p>
    </form>
    <form id="deleteCartItem"/>
</tags:master>