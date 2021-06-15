<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form method="post">
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"></tags:sortLink>
                <tags:sortLink sort="description" order="desc"></tags:sortLink>
            </td>
            <td>
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"></tags:sortLink>
                <tags:sortLink sort="price" order="desc"></tags:sortLink>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"
                        ${product.description}</td>

                <td>
                    <c:set var="quantity" value="1"/>
                    <c:set var="error" value="${errors[product.id]}"/>
                    <fmt:formatNumber value="${quantity}"/>
                    <input name="quantity"
                           value="${not empty error ? paramValues['quantity'][status.index] : quantity}">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                    <input type="hidden" name="productId" value="${product.id}">
                </td>

                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}"
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
                <td>
                    <button form="addCartItem"
                            formaction="${pageContext.servletContext.contextPath}/products/addCartItem/${product.id}}">
                        Add item to cart
                    </button>
                </td>
            </tr>
        </c:forEach>
        <form id="addCartItem"/>
    </table>

    <c:if test="${not empty recentViews}">
        <c:forEach var="view" items="${recentViews}">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${view.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${view.id}"
                        ${view.description}</td>
            </tr>
        </c:forEach>
    </c:if>
</tags:master>