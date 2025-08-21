<%@ page import="Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null){
        response.sendRedirect("login.jsp");
        return;
    }
%><title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/additem.css">


<h2>Add New Item</h2>
<form action="ItemServlet" method="post">
    <input type="hidden" name="action" value="add">
    Title: <input type="text" name="title" required><br>
    Author: <input type="text" name="author"><br>
    Category: <input type="text" name="category"><br>
    Price: <input type="number" step="0.01" name="price" required><br>
    Stock Quantity: <input type="number" name="stockQuantity" required><br>
    Date Added: <input type="date" name="dateAdded" required><br>
    <input type="submit" value="Add Item">
</form>
<a href="ItemServlet?action=list">Back to Item List</a>
