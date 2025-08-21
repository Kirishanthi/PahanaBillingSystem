<%@ page import="Bean.UserBean" %>
<%
    if (session == null || session.getAttribute("currentUser") == null) { response.sendRedirect("login.jsp"); return; }
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Help</title></head>
<body>
<h3>Help</h3>
<ul>
    <li>Login செய்து Staff Dashboard செல்லவும்.</li>
    <li>Add New Customer – புதிய customer account number, name, address, phone.</li>
    <li>Edit/View Customer – account number மூலம் load செய்து update/delete.</li>
    <li>Manage Items – புத்தக details add/update/delete + stock.</li>
    <li>Calculate & Create Bill – customer account number கொடுத்து items select செய்து bill உருவாக்கி print செய்யலாம்.</li>
</ul>
<p><a href="staff_dashboard.jsp">Back</a></p>
</body>
</html>
