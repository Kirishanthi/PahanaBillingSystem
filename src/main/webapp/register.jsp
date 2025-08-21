<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>    
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/register.css">
    
</head>
<body>
<h1>WellCome to Pahana edu!</h1>
<h2>Register</h2>

<% String error = (String) request.getAttribute("error");
   if(error != null){ %>
   <p style="color:red"><%= error %></p>
<% } %>

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
<% } %>

<p><a href="<%=request.getContextPath()%>/login.jsp">Back to Login</a></p>
</body>
</html>
