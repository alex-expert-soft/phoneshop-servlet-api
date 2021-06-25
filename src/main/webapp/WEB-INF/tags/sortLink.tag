<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?sort=${sort}&order=${order}&query=${param.query}"
   style="${sort eq param.sort and order eq param.order ? 'font-weight: bold' : ''}">
    <img src="
    ${sort eq param.sort and order eq param.order and order eq 'asc'
    ?'https://img.icons8.com/cotton/24/000000/circled-up--v3.png'
    : order eq 'asc'
    ?'https://img.icons8.com/cotton/24/000000/circled-up--v2.png'
    : sort eq param.sort and order eq param.order and order eq 'desc'
    ? 'https://img.icons8.com/cotton/24/000000/circled-down--v2.png'
    :   'https://img.icons8.com/cotton/24/000000/circled-down--v1.png'
    }" alt="${order}"/>
</a>