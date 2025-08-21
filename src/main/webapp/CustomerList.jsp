<%@ page import="Bean.CustomerBean, Bean.UserBean, java.util.List" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user==null){
        response.sendRedirect("login.jsp");
        return;
    }
    String role = user.getRole();
%>

<h2>Customer List</h2>
<a href="CustomerServlet?action=add">Add New Customer</a>

<%
    String msg = (String) session.getAttribute("message");
    if(msg != null){
        out.println("<p style='color:green'>"+msg+"</p>");
        session.removeAttribute("message");
    }
%>
<title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/customerlist.css">

<table border="1" cellpadding="5" cellspacing="0">
<tr>
    <th>ID</th>
    <th>Account No</th>
    <th>Name</th>
    <th>Address</th>
    <th>Telephone</th>
    <th>Date Registered</th>
    <% if("admin".equals(role)) { %><th>Actions</th><% } %>
</tr>
<%
    List<CustomerBean> customers = (List<CustomerBean>) request.getAttribute("customers");
    if(customers != null){
        for(CustomerBean c : customers){
%>
<tr>
    <td><%= c.getCustomerId() %></td>
    <td><%= c.getAccountNumber() %></td>
    <td><%= c.getName() %></td>
    <td><%= c.getAddress() %></td>
    <td><%= c.getTelephone() %></td>
    <td><%= c.getDateRegistered() %></td>
    <% if("admin".equals(role)) { %>
    <td>
        <a href="CustomerServlet?action=edit&id=<%=c.getCustomerId()%>">Edit</a> |
        <a href="CustomerServlet?action=delete&id=<%=c.getCustomerId()%>" onclick="return confirm('Are you sure?');">Delete</a>
    </td>
    <% } %>
</tr>
<%
        }
    }
%>
</table>
