<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Bean.BillBean, Bean.BillItemBean, java.util.*, java.math.BigDecimal" %>
<%
  Integer cid = (Integer) session.getAttribute("selfCustomerId");
  if (cid == null) { response.sendRedirect(request.getContextPath()+"/customer?action=form"); return; }
  BillBean bill = (BillBean) request.getAttribute("bill");
  if (bill == null) { response.sendRedirect(request.getContextPath()+"/customer?action=dashboard"); return; }
%>
<!DOCTYPE html><html><head><title>My Bill #<%= bill.getBillId() %></title>
<style>
body{font-family:Arial;margin:20px}.header{display:flex;justify-content:space-between;gap:24px}
table{border-collapse:collapse;width:100%;margin-top:12px}th,td{border:1px solid #ccc;padding:8px}th{background:#f5f5f5}
.tot{text-align:right;font-weight:bold}.btn{padding:8px 14px;border:1px solid #999;background:#fafafa;border-radius:4px;text-decoration:none}
</style></head><body>
<div class="header">
  <div><h2>Pahana Edu — Invoice</h2>
    <p><b>Bill #:</b> <%= bill.getBillId() %><br/><b>Date:</b> <%= bill.getBillDate() %></p></div>
  <div><p><b>Name:</b> <%= bill.getCustomer().getName() %><br/>
    <b>Account #:</b> <%= bill.getCustomer().getAccountNumber() %><br/>
    <b>Phone:</b> <%= bill.getCustomer().getTelephone() %><br/>
    <b>Address:</b> <%= bill.getCustomer().getAddress() %></p></div>
</div>
<table>
  <tr><th>#</th><th>Item</th><th>Qty</th><th>Unit Price</th><th>Line Total</th></tr>
  <%
    int i = 1; BigDecimal grand = BigDecimal.ZERO;
    for (BillItemBean bi : bill.getItems()) {
      BigDecimal line = bi.getPrice().multiply(new BigDecimal(bi.getQuantity()));
      grand = grand.add(line);
  %>
  <tr>
    <td><%= i++ %></td><td><%= bi.getTitle() %></td>
    <td><%= bi.getQuantity() %></td><td>Rs.<%= bi.getPrice() %></td><td>Rs.<%= line %></td>
  </tr>
  <% } %>
  <tr><td colspan="4" class="tot">Total</td><td class="tot">Rs.<%= grand %></td></tr>
</table>
<p style="margin-top:12px">
  <button class="btn" onclick="window.print()">Print</button>
  <a class="btn" href="<%=request.getContextPath()%>/customer?action=dashboard">Back</a>
</p>
</body></html>
