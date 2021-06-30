<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <p>
        <a href="${pageContext.servletContext.contextPath}/search">Advanced search</a>
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="error">
            There were errors adding to cart
        </div>
    </c:if>
    <form>
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
            <td class="quantity">
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
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}</a></td>

                <td>
                    <fmt:formatNumber value="1" var="quantity"/>
                    <input form="addItem/${product.id}"
                           class="quantity" type="text" name="quantity"
                           value="${products.get(status.index).id == param.productId
                           ? (not empty param.quantity ? param.quantity : 1)
                           : 1}"/>
                    <c:if test="${not empty param.error && products.get(status.index).id == param.productId}">
                        <div class="error">
                                ${param.error}
                        </div>
                    </c:if>
                    <input form="addItem/${product.id}"
                           type="hidden"
                           name="productId"
                           value="${product.id}"/>
                    <input form="addItem/${product.id}"
                           type="hidden"
                           name="redirect"
                           value="PLP"/>
                </td>

                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button form="addItem/${product.id}"
                            formaction="${pageContext.servletContext.contextPath}/add-product-to-cart/${product.id}}">
                        Add item to cart
                    </button>
                </td>
            </tr>
            <form id="addItem/${product.id}" method="post"></form>
        </c:forEach>
    </table>

    <c:if test="${not empty recentlyViewed}">
        <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
    </c:if>
</tags:master>