<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Result</title>
</head>

<body>
<h2>${message}</h2>

<form action="<c:url value="/"/>" method="get">
    <input type="submit" value="back">
</form>

</body>
</html>