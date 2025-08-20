<%@ page import="Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user==null){
        response.sendRedirect("login.jsp");
        return;
    }
%>
<h2>Add Customer</h2>
<form action="CustomerServlet" method="post">
    <input type="hidden" name="action" value="add">
    Account Number: <input type="text" name="accountNumber" required><br>
    Name: <input type="text" name="name" required><br>
    Address: <input type="text" name="address"><br>
    Telephone: <input type="text" name="telephone"><br>
    Date Registered: <input type="date" name="dateRegistered" required><br>
    <input type="submit" value="Add Customer">
</form>
<a href="CustomerServlet?action=list">Back to List</a>
