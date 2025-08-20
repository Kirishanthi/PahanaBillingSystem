<%@ page import="Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null || !"admin".equals(user.getRole())){
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
    <li><a href="<%=request.getContextPath()%>/StaffServlet?action=list">Manage Staff</a></li>
    <li><a href="<%=request.getContextPath()%>/CustomerServlet?action=list">Manage Customers</a></li>
    <li><a href="<%=request.getContextPath()%>/ItemServlet?action=list">Manage Items</a></li>
    <li><a href="<%=request.getContextPath()%>/BillServlet?action=list">View Bills</a></li>
    <li><a href="<%=request.getContextPath()%>/generatebill.jsp">Generate Bill</a></li>
    <li><a href="<%=request.getContextPath()%>/help.jsp">Help</a></li>
    <li><a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a></li>
</ul>
</body>
</html>
