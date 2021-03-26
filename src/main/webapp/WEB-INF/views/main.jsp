<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Notes</title>
</head>

<body>
<h2>Notes</h2>


<table border="1">
    <tr>
        <th>id</th>
        <th>title</th>
        <th>delete</th>
    </tr>

    <c:forEach var="note" items="${notes}">
        <tr>
            <th>
                    ${note.id}
            </th>
            <th>
                <a href="<c:url value="/notes?id=${note.id}" />">
                        ${note.title}
                </a>
            </th>
            <th>
                <form action="<c:url value="delete" />" method="post">
                    <input type="hidden" value="${note.id}" name="id">
                    <input type="submit" value="delete">
                </form>
            </th>
        </tr>
    </c:forEach>

</table>

<h2>Create new note</h2>

<form action="<c:url value="add" />" method="post">
    Title: <input type="text" name="title"> <br>
    Description: <input type="text" name="description"> <br>
    <input type="submit" value="add">
</form>

</body>
</html>