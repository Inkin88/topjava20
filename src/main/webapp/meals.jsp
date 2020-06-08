<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список еды</title>
</head>
<body>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    <c:forEach items="${mealList}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr>
            <javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm" var="parsedDate"/>
            <td>${parsedDate}
            </td>
            <td><%=meal.getDescription()%>
            </td>
            <td style="background-color:${meal.excess ? 'red' : 'darkgreen'}"><%=meal.getCalories()%>
            </td>
            <td><a href="meals?action=edit&id=${meal.id}">Edit</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=add">Add meal</a></p>
</body>
</html>
