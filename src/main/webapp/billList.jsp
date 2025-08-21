<%@ page import="java.util.List, Bean.BillBean, Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null || (!"admin".equals(user.getRole()) && !"staff".equals(user.getRole()))){
        response.sendRedirect("login.jsp");
        return;
    }

    List<BillBean> bills = (List<BillBean>) request.getAttribute("bills");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Bill List</title><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/billlist.css">
    
</head>
<body>
<h2>All Bills</h2>

<% String msg = (String) session.getAttribute("message");
   if(msg != null){ %>
   <p style="color:green;"><%= msg %></p>
<% session.removeAttribute("message"); } %>

<table border="1" cellpadding="5">
    <tr>
        <th>Bill ID</th>
        <th>Customer ID</th>
        <th>Bill Date</th>
        <th>Total Amount</th>
        <th>Actions</th>
    </tr>
    <% if(bills != null){
        for(BillBean b : bills){ %>
        <tr>
            <td><%= b.getBillId() %></td>
            <td><%= b.getCustomerId() %></td>
            <td><%= b.getBillDate() %></td>
            <td><%= b.getTotalAmount() %></td>
            <td>
                <a href="BillServlet?action=print&billId=<%= b.getBillId() %>">Print</a>
            </td>
        </tr>
    <%  } } %>
</table>

<br>
<a href="BillServlet?action=generate">Generate New Bill</a>
</body>
</html>
