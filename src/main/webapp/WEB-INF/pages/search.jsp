<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p></p>
    <form>
        <table>
            <tr>
                <td>Description<span style="color: red">*</span></td>
                <td>
                    <input name="description" value="${param.description}">
                </td>
                <td>
                    <select name="searchType">
                        <c:forEach var="searchType" items="${searchTypes}">
                            <option>${searchType.toString()}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Min price<span style="color: red">*</span></td>
                <td>
                    <input name="minPrice" value="${param.minPrice}">
                    <c:if test="${not empty errorMinPrice}">
                        <div class="error">
                                ${errorMinPrice}
                        </div>
                    </c:if>
                </td>

            </tr>
            <tr>
                <td>Max price<span style="color: red">*</span></td>
                <td>
                    <input name="maxPrice" value="${param.maxPrice}">
                    <c:if test="${not empty errorMaxPrice}">
                        <div class="error">
                                ${errorMaxPrice}
                        </div>
                    </c:if>
                </td>
            </tr>

        </table>
        <p>
        <button>Search</button>
        </p>
    </form>

    <c:if test="${not empty products}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="quantity">
                    Quantity
                </td>
                <td class="price">
                    Price
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
    </c:if>

    <c:if test="${not empty recentlyViewed}">
        <tags:recentlyViewed recentlyViewed="${recentlyViewed}"/>
    </c:if>
</tags:master>