<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Restablecer Contraseña</title>
</head>
<body>
    <h2>Restablecer Contraseña</h2>
    <form id="resetPasswordFormm" action="/Araclon/resetPassword" method="post">
        <input type="hidden" name="token" value="${token}">
        <label for="password">Nueva Contraseña:</label>
        <input type="password" name="password" id="password" required>
        <button type="submit">Establecer Contraseña</button>
    </form>
</body>
</html>