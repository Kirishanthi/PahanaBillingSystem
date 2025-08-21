<%@ page import="java.util.List, Bean.UserBean" %>
<%
    UserBean currentUser = (UserBean) session.getAttribute("currentUser");
    if(currentUser==null || !"admin".equals(currentUser.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>
<h2>Manage Staff</h2>
<%
    String msg = (String) session.getAttribute("message");
    if(msg != null){ out.println("<p style='color:green'>"+msg+"</p>"); session.removeAttribute("message"); }
    List<UserBean> staffList = (List<UserBean>) request.getAttribute("staffList");
%><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/stafflist.css">

<table border="1" cellpadding="5" cellspacing="0">
<tr>
    <th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Actions</th>
</tr>
<%
    if(staffList != null && !staffList.isEmpty()){
        for(UserBean s : staffList){
%>
<tr>
    <td><%= s.getId() %></td>
    <td><%= s.getUsername() %></td>
    <td><%= s.getEmail() %></td>
    <td><%= s.getRole() %></td>
    <td>
        <a href="StaffServlet?action=edit&id=<%= s.getId() %>">Edit</a> |
        <a href="StaffServlet?action=delete&id=<%= s.getId() %>" onclick="return confirm('Are you sure?');">Delete</a>
    </td>
</tr>
<%
        }
    } else {
%>
<tr><td colspan="5">No staff found.</td></tr>
<% } %>
</table>
