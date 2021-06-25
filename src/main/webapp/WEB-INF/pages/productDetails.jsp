<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" class="com.es.phoneshop.model.product.entity.Product" scope="request"/>
<tags:master pageTitle="Product details">
    <c:if test="${empty cart.items}">
        <div>
            <h3>
                Cart: ${cart}, total quantity: ${cart.totalQuantity}, total cost: ${cart.totalCost}
            </h3>
        </div>
    </c:if>

    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="error">
            There was an error adding to cart
        </div>
    </c:if>

    <p>
            ${product.description}
    </p>
    <form method="post" action="${pageContext.servletContext.contextPath}/add-product-to-cart/${product.id}">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>price</td>
                <td>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>quantity</td>
                <td>
                    <input class="quantity" type="text" name="quantity"
                           value="${not empty param.quantity ? param.quantity : 1}">
                    <input type="hidden" name="redirect" value="PDP">
                    <input type="hidden" name="productId" value="${product.id}">
                    <c:if test="${not empty param.error}">
                        <div class="error">
                                ${param.error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>

    <c:if test="${not empty recentlyViewed}">
        <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
    </c:if>
</tags:master>