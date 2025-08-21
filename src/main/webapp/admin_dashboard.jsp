<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="Bean.UserBean" %>
<%
    // Admin guard
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if (user == null || user.getRole() == null || !"admin".equalsIgnoreCase(user.getRole().trim())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ta">
<head>
  <meta charset="UTF-8">
  <title>Admin Dashboard</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Icons + CSS -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Outlined" rel="stylesheet">
  <link rel="stylesheet" href="<%=ctx%>/css/admin.css">
</head>
<body>

<!-- Top Nav (NO welcome in nav) -->
<nav class="topnav">
  <div class="brand">
    <a href="<%=ctx%>/AdminDashboard.jsp" class="logo">
      <span class="mi">menu_book</span> Pahana Edu
    </a>
  </div>

  <div class="navlinks">
    <a href="<%=ctx%>/StaffServlet?action=list"><span class="mi">groups</span> Manage Staff</a>
    <a href="<%=ctx%>/CustomerServlet?action=list"><span class="mi">badge</span> Manage Customers</a>
    <a href="<%=ctx%>/ItemServlet?action=list"><span class="mi">inventory_2</span> ManageItems</a>
    <a href="<%=ctx%>/BillServlet?action=list"><span class="mi">receipt_long</span> Bills</a>
    <a href="<%=ctx%>/BillServlet?action=form"><span class="mi">point_of_sale</span> Generate Bill</a>
    <a href="<%=ctx%>/help.jsp"><span class="mi">help_center</span> Help</a>
  </div>

  <div class="nav-cta">
    <a href="<%=ctx%>/LogoutServlet" class="btn-out"><span class="mi">logout</span> Logout</a>
  </div>
</nav>

<!-- Centered Welcome block -->
<section class="hero">
  <div class="hero-card">
    <span class="mi hero-icon">admin_panel_settings</span>
    <h1>Welcome, <%= user.getUsername() %></h1>
    <p class="muted">Admin Dashboard</p>
  </div>
</section>

</body>
</html>
