<%@ page import="Bean.UserBean" %>
<%
    // Session check
    if(session == null || session.getAttribute("currentUser") == null){
        response.sendRedirect("login.jsp");
        return;
    }

    UserBean user = (UserBean) session.getAttribute("currentUser");

    // Role check: only admin can access this page
    if(!"admin".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; }
        h2 { color: #333; }
        ul { list-style-type: none; padding: 0; }
        li { margin: 10px 0; }
        a { text-decoration: none; color: #0066cc; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<h2>Welcome, <%= user.getUsername() %> (Admin)</h2>

<ul>
    <!-- Manage Staff: Only View/Edit/Delete -->
    <li><a href="StaffServlet?action=list">Manage Staff</a></li>

    <!-- Manage Customers -->
    <li><a href="AddCustomerServlet?action=list">Manage Customers</a></li>

    <!-- Manage Items -->
    <li><a href="ItemServlet?action=list">Manage Items</a></li>

    <!-- Generate / View Bills -->
    <li><a href="generatebill.jsp">Generate Bill</a></li>
    <li><a href="BillServlet?action=list">View Bills</a></li>

    <!-- Reports -->
    <li><a href="report.jsp">View Reports</a></li>

    <!-- Help -->
    <li><a href="help.jsp">Help</a></li>

    <!-- Logout -->
    <li><a href="LogoutServlet">Logout</a></li>
</ul>

</body>
</html>
