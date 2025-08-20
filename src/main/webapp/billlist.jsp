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
    <title>Bill List</title>
</head>
<body>
<h2>Bill List</h2>

<%
    String message = (String) session.getAttribute("message");
    if(message != null){
%>
    <p style="color:green;"><%= message %></p>
<%
        session.removeAttribute("message");
    }
%>

<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>Bill ID</th>
        <th>Customer</th>
        <th>Bill Date</th>
        <th>Total Amount</th>
        <th>Action</th>
    </tr>
<%
    if(bills != null){
        for(BillBean b : bills){
%>
    <tr>
        <td><%= b.getBillId() %></td>
        <td><%= b.getCustomerId() %></td>
        <td><%= b.getBillDate() %></td>
        <td><%= String.format("%.2f", b.getTotalAmount()) %></td>
        <td>
            <a href="BillServlet?action=print&billId=<%= b.getBillId() %>" target="_blank">Print</a>
        </td>
    </tr>
<%
        }
    }
%>
</table>

<br>
<a href="BillServlet?action=generate">Generate New Bill</a>

<br><br>
<a href="<%= user.getRole().equals("admin") ? "adminDashboard.jsp" : "staffDashboard.jsp" %>">Back to Dashboard</a>

</body>
</html>
