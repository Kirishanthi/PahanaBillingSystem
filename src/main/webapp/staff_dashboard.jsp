<%
    if(session == null || session.getAttribute("currentUser") == null){
        response.sendRedirect("login.jsp");
        return;
    }
    Bean.UserBean user = (Bean.UserBean) session.getAttribute("currentUser");
    if(!"staff".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head><title>Staff Dashboard</title></head>
<body>
<h2>Welcome, <%= user.getUsername() %> (Staff)</h2>
<ul>
    <li><a href="CustomerServlet?action=add">Add Customer</a></li>
    <li><a href="CustomerServlet?action=list">View Customers</a></li>
    <li><a href="ItemServlet?action=list">View Items</a></li>
    <li><a href="generatebill.jsp">Generate Bill</a></li>
    <li><a href="printbill.jsp">Print Bill</a></li>
    <li><a href="help.jsp">Help</a></li>
    <li><a href="LogoutServlet">Logout</a></li>
</ul>
</body>
</html>
