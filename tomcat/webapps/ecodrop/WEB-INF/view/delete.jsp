<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Supprimer</title></head>
<body>
<h1>Supprimer</h1>
<jsp:include page="menu.jsp"/>

<form method='post' action='Control'>
    <input type='hidden' name='action' value='delete'>
    <p>Rentrez l'id de partie voulu</p>
    <input type='text' name='pno'>
    <input type='submit'>
</form>
</body>
</html>
