<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.entity.Product" scope="request"/>
<tags:master pageTitle="Product details">
    <h3>Price history</h3>
    <h2>
            ${product.description}
    </h2>


    <table>
        <thead>
        <tr>
            <td>
                Start date
            </td>
            <td>
                Price
            </td>
        </tr>
        </thead>
        <c:forEach var="history" items="${product.priceHistory}">
            <tr>
                <td>
                        ${history.dateTime}
                </td>
                <td>
                        ${history.price}
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>