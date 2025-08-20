<%@ page import="java.util.List, Bean.BillBean, Bean.BillItemBean, Bean.CustomerBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null || (!"admin".equals(user.getRole()) && !"staff".equals(user.getRole()))){
        response.sendRedirect("login.jsp");
        return;
    }

    BillBean bill = (BillBean) request.getAttribute("bill");
    List<BillItemBean> items = (List<BillItemBean>) request.getAttribute("items");
    CustomerBean customer = (CustomerBean) request.getAttribute("customer");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Print Bill</title>
    <style>
        table { border-collapse: collapse; width: 70%; }
        table, th, td { border: 1px solid black; }
        th, td { padding: 8px; text-align: left; }
        h2, h3 { margin: 0; }
        .center { text-align: center; }
    </style>
</head>
<body>
<div class="center">
    <h2>Company Name / Shop Name</h2>
    <h3>Bill Receipt</h3>
</div>

<p><strong>Bill ID:</strong> <%= bill.getBillId() %></p>
<p><strong>Customer:</strong> <%= customer.getName() %> (<%= customer.getAccountNumber() %>)</p>
<p><strong>Bill Date:</strong> <%= bill.getBillDate() %></p>

<table>
    <tr>
        <th>S.No</th>
        <th>Item</th>
        <th>Quantity</th>
        <th>Price</th>
        <th>Total</th>
    </tr>
<%
    int serial = 1;
    double grandTotal = 0;
    if(items != null){
        for(BillItemBean bi : items){
            grandTotal += bi.getTotalAmount();
%>
    <tr>
        <td><%= serial++ %></td>
        <td><%= bi.getItemName() %></td>
        <td><%= bi.getQuantity() %></td>
        <td><%= String.format("%.2f", bi.getPrice()) %></td>
        <td><%= String.format("%.2f", bi.getTotalAmount()) %></td>
    </tr>
<%
        }
    }
%>
    <tr>
        <td colspan="4" style="text-align:right;"><strong>Grand Total:</strong></td>
        <td><strong><%= String.format("%.2f", grandTotal) %></strong></td>
    </tr>
</table>

<br>
<div class="center">
    <button onclick="window.print()">Print Bill</button>
    <a href="BillServlet?action=list">Back to Bill List</a>
</div>
</body>
</html>
