<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/register.css">

</head>
<body>

<h2>Pahana Edu - Login</h2>

<% String status = request.getParameter("login");
   if("fail".equals(status)) { %>
       <p style="color:red">Login Failed! Check username/password.</p>
<% } %>

<form action="/PahanaBillingSystem/LoginServlet" method="post">
    Username: <input type="text" name="username" required><br><br>
    Password: <input type="password" name="password" required><br><br>
    <input type="submit" value="Login">
</form>

<p>New Staff? <a href="/PahanaBillingSystem/register.jsp">Register Here</a></p>

</body>
</html>
