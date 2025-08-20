<%@ page import="Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user==null || !"staff".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>

<h2>Add Item</h2>
<%
    String msg = (String) session.getAttribute("message");
    if(msg!=null){ out.println("<p style='color:green'>"+msg+"</p>"); session.removeAttribute("message"); }
%>

<form action="<%=request.getContextPath()%>/ItemServlet" method="post">
    <input type="hidden" name="action" value="add">
    Title: <input type="text" name="title" required><br>
    Author: <input type="text" name="author" required><br>
    Category: <input type="text" name="category" required><br>
    Price: <input type="number" step="0.01" name="price" required><br>
    Stock Quantity: <input type="number" name="stockQuantity" required><br>
    Date Added: <input type="date" name="dateAdded" required><br><br>
    <input type="submit" value="Add Item">
</form>

<a href="itemList.jsp">View Items</a>
