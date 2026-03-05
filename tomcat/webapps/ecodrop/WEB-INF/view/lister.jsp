<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.*, model.*" %>
<!DOCTYPE html>
<html>
<head><title>Lister</title></head>
<body>
<h1>Lister</h1>
<jsp:include page="menu.jsp"/>

<%
List<Partie> parties = (List<Partie>) request.getAttribute("parties");
%>

<h2>Parties</h2>
<table>
<% for(Partie partie : parties) { %>
    <tr><td><%= partie %></td></tr>
<% } %>
</table>
</body>
</html>
