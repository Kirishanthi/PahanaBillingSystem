<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="Bean.UserBean" %>
<%
    if (session == null || session.getAttribute("currentUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Bean.UserBean user = (Bean.UserBean) session.getAttribute("currentUser");
    if (!"staff".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Staff Dashboard</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <!-- CSS (separate file) -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/staff.css" />
  <!-- Icons -->
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
        referrerpolicy="no-referrer" />
</head>
<body>

  <!-- Top Navbar -->
  <header class="topbar">
    <div class="brand">
      <i class="fa-solid fa-book-open"></i>
      <span>Pahana Edu</span>
    </div>

    <!-- STAFF FUNCTIONS ONLY -->
    <nav class="menu" aria-label="Staff menu">
      <a href="CustomerServlet?action=add">
        <i class="fa-solid fa-user-plus"></i><span>Add Customer</span>
      </a>
      <a href="CustomerServlet?action=list">
        <i class="fa-solid fa-users"></i><span>View Customers</span>
      </a>
      <a href="ItemServlet?action=list">
        <i class="fa-solid fa-boxes-stacked"></i><span>View Items</span>
      </a>
      <a href="generatebill.jsp">
        <i class="fa-solid fa-file-invoice-dollar"></i><span>Generate Bill</span>
      </a>
      <a href="printbill.jsp">
        <i class="fa-solid fa-print"></i><span>Print Bill</span>
      </a>
      <a href="help.jsp">
        <i class="fa-solid fa-circle-question"></i><span>Help</span>
      </a>
    </nav>

    <div class="actions">
      <a class="btn-logout" href="LogoutServlet">
        <i class="fa-solid fa-right-from-bracket"></i><span>Logout</span>
      </a>
    </div>
  </header>

  <!-- Center Welcome Card -->
  <main class="container">
    <section class="hero-card" aria-label="Welcome">
      <div class="hero-left">
        <i class="fa-solid fa-shield-halved"></i>
      </div>
      <div class="hero-right">
        <div class="welcome-pill">Welcome, <%= user.getUsername() %></div>
        <p class="sub">Staff Dashboard</p>
      </div>
    </section>
  </main>

</body>
</html>
