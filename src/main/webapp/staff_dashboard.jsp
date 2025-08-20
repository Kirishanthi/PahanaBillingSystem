<%@ page import="Bean.UserBean" %>
<%
    // Session check
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null || !"staff".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Staff Dashboard</title>
</head>
<body>
<h2>Welcome, <%= user.getUsername() %> (Staff)</h2>

<ul>
    <!-- Staff functions -->
    <li><a href="addcustomer.jsp">Add Customer</a></li>
    <li><a href="AddCustomerServlet?action=list">View Customers</a></li>
    <li><a href="ItemServlet?action=list">View Items</a></li>
    <li><a href="generatebill.jsp">Generate Bill</a></li>
    <li><a href="BillServlet?action=list">View Bills</a></li>

    <!-- Help / Logout -->
    <li><a href="help.jsp">Help</a></li>
    <li><a href="LogoutServlet">Logout</a></li>
</ul>

</body>
</html>
