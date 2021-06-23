<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>

<c:if test="${not empty cart.items}">
    <a href="${pageContext.servletContext.contextPath}/cart">
        Cart: ${cart.totalQuantity} items ,
        <fmt:formatNumber value="${cart.totalCost}" type="currency"
                          currencySymbol="${cart.currency.symbol}"/>
        cost
    </a>
</c:if>