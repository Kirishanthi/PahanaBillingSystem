<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pahana Edu - Login</title>
</head>
<body>

<h2>Pahana Edu - Login</h2>

<!-- Display messages from Servlet -->
<p style="color:red;">${errorMessage}</p>
<p style="color:green;">${message}</p>

<!-- Login Form -->
<form action="/PahanaBillingSystem/LoginServlet" method="post">
    Email: <input type="email" name="email" required><br><br>
    Password: <input type="password" name="password" required><br><br>
    <input type="submit" value="Login">
</form>

<% String status = request.getParameter("login");
   if("fail".equals(status)) { %>
       <p style="color:red">Login Failed! Check email/password.</p>
<% } %>


<p>New Staff? <a href="/PahanaBillingSystem/jsp/register.jsp">Register Here</a></p>

</body>
</html>
