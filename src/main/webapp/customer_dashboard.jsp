<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="Bean.CustomerBean, Bean.BillBean, java.util.*" %>
<%
    // must come through servlet after verify
    Integer cid = (Integer) session.getAttribute("selfCustomerId");
    if (cid == null) {
        response.sendRedirect(request.getContextPath() + "/CustomerPortalServlet?action=form");
        return;
    }

    // data should be set by servlet; if not, re-route to servlet to load
    List<BillBean> bills = (List<BillBean>) request.getAttribute("bills");
    CustomerBean customer = (CustomerBean) request.getAttribute("customer");
    if (bills == null || customer == null) {
        response.sendRedirect(request.getContextPath() + "/customer?action=dashboard");
        return;
    }

    String err = (String) session.getAttribute("error");
    if (err != null) session.removeAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Account</title>
    <style>
        body{ font-family: Arial, sans-serif; margin: 20px; }
        table{ border-collapse: collapse; width:100%; margin-top:10px; }
        th,td{ border:1px solid #ccc; padding:8px; }
        th{ background:#f5f5f5; }
        .btn{ display:inline-block; padding:6px 10px; border:1px solid #999; background:#fafafa; border-radius:4px; text-decoration:none; }
        .meta p{ margin:4px 0; }
        .err{ background:#fff1f0; border:1px solid #ffa39e; padding:10px; border-radius:4px; margin-bottom:10px; }
    </style>
</head>
<body>
<h2>My Account</h2>

<% if (err != null) { %><div class="err"><%= err %></div><% } %>

<div class="meta">
    <p><strong>Name:</strong> <%= customer.getName() %></p>
    <p><strong>Account #:</strong> <%= customer.getAccountNumber() %></p>
    <p><strong>Phone:</strong> <%= customer.getTelephone() %></p>
    <p><strong>Address:</strong> <%= customer.getAddress() %></p>
</div>

<h3>My Bills</h3>
<table>
    <tr><th>Bill #</th><th>Date</th><th>Total</th><th>Action</th></tr>
    <% if (bills != null && !bills.isEmpty()) {
           for (BillBean b : bills) { %>
        <tr>
            <td><%= b.getBillId() %></td>
            <td><%= b.getBillDate() %></td>
            <td>Rs.<%= b.getTotalAmount() %></td>
            <td><a class="btn" href="<%=request.getContextPath()%>/customer?action=bill&billId=<%= b.getBillId() %>">View / Print</a></td>
        </tr>
    <% } } else { %>
        <tr><td colspan="4">No bills found.</td></tr>
    <% } %>
</table>

<p style="margin-top:12px;">
    <a class="btn" href="<%=request.getContextPath()%>/customer?action=logout">Exit</a>
</p>
</body>
</html>
