<%--
  Created by IntelliJ IDEA.
  User: dmitriy
  Date: 13.06.2022
  Time: 23:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<hr>
<a href="meals?action=create">Add Meal</a>
<h3>Meals</h3>

<table border="1" cellpadding = "8" cellspacing="0">
    <thead>
    <tr>
        <th>ID</th>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
        <%--    <th>DOB</th>
            <th>Email</th>  --%>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${mealsList}" var="meal">
        <%--<span style="color: ${meal.isExcess()?"red":"green"} ">--%>

        <tr style="color: ${meal.excess?"red":"green"}">
            <td><c:out value="${meal.id}"/></td>

            <td>
                <fmt:parseDate value="${meal.dateTime}" type="both" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDate}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
                <%--  <td><fmt:formatDate pattern="yyyy-MMM-dd" value="${user.dob}" /></td>
                  <td><c:out value="${user.email}" /></td>   --%>
            <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>

        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
