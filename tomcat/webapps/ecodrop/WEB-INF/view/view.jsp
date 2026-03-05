<%@ page pageEncoding="UTF-8" %>
<%@ page import="model.*" %>
<!DOCTYPE html>
<html>
<head><title>Voir</title></head>
<body>
<h1>Voir</h1>
<jsp:include page="menu.jsp"/>

<form method='post' action='Control'>
    <input type='hidden' name='action' value='voir'>
    <p>Rentrez l'id de partie voulu</p>
    <input type='text' name='n'>
    <input type='submit'>
</form>

<%
Partie partie = (Partie) request.getAttribute("partie");
if (partie != null) {
%>
    <h2>Résultat</h2>
    <p><%= partie %></p>
<%
}
%>
</body>
</html>
