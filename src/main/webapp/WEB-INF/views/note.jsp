<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${note.title}</title>
</head>

<body>
<h2>${note.title}</h2>

<p>${note.description}</p>

<p><em>${note.dateTime}</em></p>

<form action="<c:url value="/"/>" method="get">
    <input type="submit" value="back">
</form>

</body>
</html>