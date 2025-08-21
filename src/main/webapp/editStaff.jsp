<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="Bean.UserBean" %>
<%
    // Admin-only guard
    UserBean currentUser = (UserBean) session.getAttribute("currentUser");
    if (currentUser == null || !"admin".equals(currentUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Staff object from servlet
    UserBean staff = (UserBean) request.getAttribute("staff");
    if (staff == null) {
        session.setAttribute("message", "Staff not found.");
        response.sendRedirect("StaffServlet?action=list");
        return;
    }

    String msg = (String) session.getAttribute("message");
    if (msg != null) {
        session.removeAttribute("message");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Staff</title><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/editstaff.css">
    
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .wrap { max-width: 520px; }
        label { display: block; margin-top: 12px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; box-sizing: border-box; }
        .actions { margin-top: 16px; }
        .btn { display: inline-block; padding: 8px 14px; border: 1px solid #999; border-radius: 4px; text-decoration: none; background: #fafafa; }
        .btn-primary { border-color: #1890ff; background: #e6f7ff; }
        .flash { padding: 10px; margin-bottom: 12px; border-radius: 4px; background: #e6ffed; border: 1px solid #b7eb8f; }
    </style>
</head>
<body>
<div class="wrap">
    <h2>Edit Staff</h2>

    <% if (msg != null) { %>
        <div class="flash"><%= msg %></div>
    <% } %>

    <form action="StaffServlet" method="post">
        <input type="hidden" name="id" value="<%= staff.getId() %>"/>

        <label for="username">Username</label>
        <input id="username" name="username" type="text" value="<%= staff.getUsername() %>" required/>

        <label for="email">Email</label>
        <input id="email" name="email" type="email" value="<%= staff.getEmail() %>" required/>

        <label for="role">Role</label>
        <select id="role" disabled>
            <option value="staff" selected>Staff</option>
        </select>
        <!-- role stays 'staff' in DAO; no need to submit it -->

        <div class="actions">
            <button type="submit" class="btn btn-primary">Update Staff</button>
            <a class="btn" href="StaffServlet?action=list">Back to Staff List</a>
        </div>
    </form>
</div>
</body>
</html>
