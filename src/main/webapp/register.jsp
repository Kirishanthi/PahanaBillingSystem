<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>
<body>
<h2>Register</h2>

<form action="<%=request.getContextPath()%>/RegisterServlet" method="post">
    Username: <input type="text" name="username" required><br><br>
    Email: <input type="email" name="email" required><br><br>
    Password: <input type="password" name="password" required><br><br>
    <input type="submit" value="Register">
</form>

<% 
String status = request.getParameter("register");
if("success".equals(status)) { 
%>
    <p style="color:green">Registration Successful! Please <a href="login.jsp">login</a></p>
<% 
} else if("fail".equals(status)) { 
%>
    <p style="color:red">Registration Failed! Try again.</p>
<% } %>


 <a href="/PahanaBillingSystem/login.jsp">Back to Login</a></p>

</body>
</html>
