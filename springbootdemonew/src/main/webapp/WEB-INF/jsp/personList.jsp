<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>Person List</h1>

<br/><br/>
<div>
    <table border="1">
        <tr>
            <th>First Name</th>
            <th>Last Name</th>
        </tr>

        <c:forEach  items="${persons}" var ="person">
            <tr>
                <td>${person.firstName}</td>
                <td>${person.lastName}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>