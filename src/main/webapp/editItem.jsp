<%@ page import="Bean.ItemBean, Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null || !"admin".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
    ItemBean i = (ItemBean) request.getAttribute("item");
%>
<title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/register.css">

<h2>Edit Item</h2>
<form action="ItemServlet" method="post">
    <input type="hidden" name="action" value="edit">
    <input type="hidden" name="itemId" value="<%= i.getItemId() %>">
    Title: <input type="text" name="title" value="<%= i.getTitle() %>" required><br>
    Author: <input type="text" name="author" value="<%= i.getAuthor() %>"><br>
    Category: <input type="text" name="category" value="<%= i.getCategory() %>"><br>
    Price: <input type="number" step="0.01" name="price" value="<%= i.getPrice() %>" required><br>
    Stock Quantity: <input type="number" name="stockQuantity" value="<%= i.getStockQuantity() %>" required><br>
    Date Added: <input type="date" name="dateAdded" value="<%= i.getDateAdded() %>" required><br>
    <input type="submit" value="Update Item">
</form>
<a href="ItemServlet?action=list">Back to Item List</a>
