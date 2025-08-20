<%
    if(session == null || session.getAttribute("currentUser") == null){
        response.sendRedirect("login.jsp");
        return;
    }
    Bean.UserBean user = (Bean.UserBean) session.getAttribute("currentUser");
    if(!"admin".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head><title>Admin Dashboard</title></head>
<body>
<h2>Welcome, <%= user.getUsername() %> (Admin)</h2>
<ul>
    <li><a href="StaffServlet?action=list">Manage Staff</a></li>
    <li><a href="CustomerServlet?action=list">Manage Customers</a></li>
    <li><a href="ItemServlet?action=list">Manage Items</a></li>
    <li><a href="BillServlet?action=list">View Bills</a></li>
    <li><a href="generatebill.jsp">Generate Bill</a></li>
    <li><a href="report.jsp">View Reports</a></li>
    <li><a href="help.jsp">Help</a></li>
    <li><a href="LogoutServlet">Logout</a></li>
</ul>
</body>
</html>
