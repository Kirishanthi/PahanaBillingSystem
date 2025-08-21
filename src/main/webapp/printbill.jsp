<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="Bean.BillBean, Bean.BillItemBean, Bean.UserBean" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%
    // Access control: admin + staff only
    UserBean currentUser = (UserBean) session.getAttribute("currentUser");
    if (currentUser == null || currentUser.getRole() == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String role = currentUser.getRole().trim().toLowerCase();
    if (!role.equals("admin") && !role.equals("staff")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    BillBean bill = (BillBean) request.getAttribute("bill");
    if (bill == null) {
        response.sendRedirect(request.getContextPath() + "/BillServlet?action=form");
        return;
    }

    List<BillItemBean> items = bill.getItems();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Print Bill #<%= bill.getBillId() %></title><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/printtbill.css">
    
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { display:flex; justify-content:space-between; gap: 24px; }
        table { border-collapse: collapse; width: 100%; margin-top: 14px; }
        th, td { border: 1px solid #ccc; padding: 8px; }
        th { background: #f5f5f5; }
        .tot { text-align: right; font-weight: bold; }
        .controls { margin-top: 16px; }
        .btn { padding:8px 14px; border:1px solid #999; background:#fafafa; border-radius:4px; text-decoration:none; cursor:pointer; }
    </style>
</head>
<body>
<div class="header">
    <div>
        <h2>Pahana Edu — Invoice</h2>
        <p><strong>Bill #:</strong> <%= bill.getBillId() %><br/>
           <strong>Date:</strong> <%= bill.getBillDate() %></p>
    </div>
    <div>
        <p><strong>Customer:</strong> <%= bill.getCustomer().getName() %><br/>
           <strong>Account #:</strong> <%= bill.getCustomer().getAccountNumber() %><br/>
           <strong>Phone:</strong> <%= bill.getCustomer().getTelephone() %><br/>
           <strong>Address:</strong> <%= bill.getCustomer().getAddress() %></p>
    </div>
</div>

<table>
    <tr>
        <th>#</th><th>Item</th><th>Qty</th><th>Unit Price</th><th>Line Total</th>
    </tr>
    <%
        int i = 1;
        BigDecimal grand = BigDecimal.ZERO;
        if (items != null) {
            for (BillItemBean bi : items) {
                // BigDecimal arithmetic
                BigDecimal line = bi.getPrice().multiply(new BigDecimal(bi.getQuantity()));
                grand = grand.add(line);
    %>
    <tr>
        <td><%= i++ %></td>
        <td><%= bi.getTitle() %></td>
        <td><%= bi.getQuantity() %></td>
        <td>Rs.<%= bi.getPrice() %></td>
        <td>Rs.<%= line %></td>
    </tr>
    <%
            }
        }
    %>
    <tr>
        <td colspan="4" class="tot">Total</td>
        <td class="tot">Rs.<%= grand %></td>
    </tr>
</table>

<div class="controls">
    <button class="btn" onclick="window.print()">Print</button>
    <a class="btn" href="<%= request.getContextPath() %>/BillServlet?action=form">New Bill</a>
</div>
</body>
</html>
