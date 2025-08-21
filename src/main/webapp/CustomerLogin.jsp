<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String err = (String) session.getAttribute("error");
  if (err != null) session.removeAttribute("error");
%>
<!DOCTYPE html><html><head><title>Customer Self-Service</title>
<style>
body{font-family:Arial;margin:30px}.wrap{max-width:420px;margin:auto}
label{display:block;margin:10px 0 6px;font-weight:bold}
input{width:100%;padding:8px;box-sizing:border-box}
.btn{margin-top:12px;padding:8px 14px;border:1px solid #999;background:#fafafa;border-radius:4px;cursor:pointer}
.err{margin-top:10px;background:#fff1f0;border:1px solid #ffa39e;padding:10px;border-radius:4px}
</style></head><body>
<div class="wrap">
  <h2>Customer Self-Service</h2>
  <p>Enter your <b>Account Number</b> and <b>Phone</b>.</p>
  <% if (err != null) { %><div class="err"><%= err %></div><% } %>
  <form action="<%=request.getContextPath()%>/customer" method="post">
    <input type="hidden" name="action" value="verify"/>
    <label>Account Number</label>
    <input name="accountNumber" type="text" required placeholder="ACC-0001"/>
    <label>Phone</label>
    <input name="telephone" type="text" required placeholder="07XXXXXXXX"/>
    <button class="btn" type="submit">View My Details</button>
  </form>
</div>
</body></html>
