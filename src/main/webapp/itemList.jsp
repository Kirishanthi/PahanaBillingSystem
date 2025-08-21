<%@ page import="java.util.List, Bean.ItemBean, Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null){
        response.sendRedirect("login.jsp");
        return;
    }
    String role = user.getRole();
%>

<h2>Item List</h2>

<%
    String msg = (String) session.getAttribute("message");
    if(msg != null){
        out.println("<p style='color:green'>" + msg + "</p>");
        session.removeAttribute("message");
    }
%>

<a href="ItemServlet?action=add">Add New Item</a>
<table border="1" cellpadding="5" cellspacing="0">
<tr>
    <th>ID</th>
    <th>Title</th>
    <th>Author</th>
    <th>Category</th>
    <th>Price</th>
    <th>Stock Qty</th>
    <th>Date Added</th>
    <% if("admin".equals(role)){ %><th>Actions</th><% } %>
</tr>
<%
    List<ItemBean> items = (List<ItemBean>) request.getAttribute("items");
    if(items != null){
        for(ItemBean i : items){
%><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/itemlist.css">

<tr>
    <td><%= i.getItemId() %></td>
    <td><%= i.getTitle() %></td>
    <td><%= i.getAuthor() %></td>
    <td><%= i.getCategory() %></td>
    <td><%= i.getPrice() %></td>
    <td><%= i.getStockQuantity() %></td>
    <td><%= i.getDateAdded() %></td>
    <% if("admin".equals(role)){ %>
    <td>
        <a href="ItemServlet?action=edit&id=<%= i.getItemId() %>">Edit</a> |
        <a href="ItemServlet?action=delete&id=<%= i.getItemId() %>" 
           onclick="return confirm('Are you sure?');">Delete</a>
    </td>
    <% } %>
</tr>
<%
        }
    }
%>
</table>
