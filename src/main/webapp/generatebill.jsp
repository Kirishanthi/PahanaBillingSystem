<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="Bean.UserBean, Bean.ItemBean, Bean.CartItemBean" %>
<%
    // --- Access control: allow only admin/staff ---
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

    // If someone opened this JSP directly, redirect to BillServlet to populate 'items'
    List<ItemBean> items = (List<ItemBean>) request.getAttribute("items");
    if (items == null) {
        response.sendRedirect(request.getContextPath() + "/BillServlet?action=form");
        return;
    }

    // Session cart + flash messages
    List<CartItemBean> cart = (List<CartItemBean>) session.getAttribute("cart");
    String msg = (String) session.getAttribute("message");
    String err = (String) session.getAttribute("error");
    if (msg != null) session.removeAttribute("message");
    if (err != null) session.removeAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Generate Bill</title><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/generatebill.css">
    
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .row { display: flex; gap: 24px; flex-wrap: wrap; }
        .col { flex: 1 1 380px; }
        table { border-collapse: collapse; width: 100%; margin-top: 10px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f5f5f5; }
        label { display:block; margin: 10px 0 6px; font-weight: bold; }
        select, input[type="number"], input[type="text"] { width: 100%; padding: 8px; box-sizing: border-box; }
        .flash { padding:10px; border-radius:4px; margin-bottom:10px; }
        .ok { background:#e6ffed; border:1px solid #b7eb8f; }
        .err{ background:#fff1f0; border:1px solid #ffa39e; }
        .btn { display:inline-block; padding:8px 14px; border:1px solid #999; background:#fafafa; border-radius:4px; text-decoration:none; cursor:pointer; }
        .btn-primary { border-color:#1890ff; background:#e6f7ff; }
        .controls { margin-top: 16px; display:flex; gap:12px; flex-wrap: wrap; }
        h2 { margin-top: 0; }
    </style>
</head>
<body>

<h2>Generate Bill</h2>
<p style="color:#666;margin-top:-6px;">Logged in as: <strong><%= currentUser.getUsername() %></strong> (<%= currentUser.getRole() %>)</p>

<% if (msg != null) { %><div class="flash ok"><%= msg %></div><% } %>
<% if (err != null) { %><div class="flash err"><%= err %></div><% } %>

<div class="row">
    <!-- Add Item -->
    <div class="col">
        <h3>Add Item</h3>
        <form action="<%= request.getContextPath() %>/BillServlet" method="post">
            <input type="hidden" name="action" value="add"/>
            <label for="itemId">Item</label>
            <select id="itemId" name="itemId" required>
                <option value="">-- Select an item --</option>
                <% for (ItemBean it : items) { %>
                    <option value="<%= it.getItemId() %>">
                        <%= it.getTitle() %> (Rs.<%= it.getPrice() %>, Stock: <%= it.getStockQuantity() %>)
                    </option>
                <% } %>
            </select>

            <label for="qty">Quantity</label>
            <input id="qty" name="quantity" type="number" min="1" value="1" required/>

            <div class="controls">
                <button type="submit" class="btn btn-primary">Add to Bill</button>
                <a class="btn" href="<%= request.getContextPath() %>/BillServlet?action=form">Refresh</a>
            </div>
        </form>
    </div>

    <!-- Cart / Bill Items -->
    <div class="col">
        <h3>Bill Items</h3>
        <table>
            <tr>
                <th>Item</th><th>Unit Price</th><th>Qty</th><th>Line Total</th><th>Action</th>
            </tr>
            <%
                BigDecimal grand = new BigDecimal("0");
                if (cart != null && !cart.isEmpty()) {
                    for (CartItemBean ci : cart) {
                        BigDecimal lt = ci.getUnitPrice().multiply(new BigDecimal(ci.getQuantity()));
                        grand = grand.add(lt);
            %>
            <tr>
                <td><%= ci.getTitle() %></td>
                <td>Rs.<%= ci.getUnitPrice() %></td>
                <td><%= ci.getQuantity() %></td>
                <td>Rs.<%= lt %></td>
                <td>
                    <a class="btn"
                       href="<%= request.getContextPath() %>/BillServlet?action=remove&itemId=<%= ci.getItemId() %>"
                       onclick="return confirm('Remove this item?');">
                        Remove
                    </a>
                </td>
            </tr>
            <%  }
               } else { %>
            <tr><td colspan="5">No items added yet.</td></tr>
            <% } %>
            <tr>
                <th colspan="3" style="text-align:right">Grand Total</th>
                <th colspan="2">Rs.<%= grand %></th>
            </tr>
        </table>
    </div>
</div>

<hr/>

<!-- Customer + Save -->
<h3>Customer & Save</h3>
<form action="<%= request.getContextPath() %>/BillServlet" method="post">
    <input type="hidden" name="action" value="save"/>
    <label for="accNo">Customer Account Number</label>
    <input id="accNo" name="accountNumber" type="text" placeholder="e.g. ACC-0001" required/>

    <div class="controls">
        <button type="submit" class="btn btn-primary">Save &amp; Print</button>
        <a class="btn" href="<%= request.getContextPath() %>/BillServlet?action=form">New/Reset</a>
        <a class="btn" href="<%= request.getContextPath() %>/admin_dashboard.jsp">Back</a>
    </div>
</form>

</body>
</html>
