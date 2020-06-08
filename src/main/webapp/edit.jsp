<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Редактирование</title>
</head>
<body>
<form action="meals" method="post">
    <input type="hidden" name="id" value="${meal.id}">
    Дата:<input type="datetime-local" name="localDateTime" value="${meal.dateTime}">
    Описание:<input type="text" name="description" value="${meal.description}">
    Калории:<input type="number" name="calories" value="${meal.calories}">
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()">Отменить</button>
</form>
</body>
</html>
