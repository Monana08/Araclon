<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<script>
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
    const messageDiv = document.getElementById('message');
 
    form.addEventListener('submit', async (event) => {
        event.preventDefault(); // Previene el envío por defecto del formulario
 
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
 
        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });
 
            if (!response.ok) {
                throw new Error('Error en la autenticación');
            }
 
            const isAuthenticated = await response.json();
 
            if (isAuthenticated) {
                messageDiv.textContent = 'Inicio de sesión exitoso!';
                window.location.href='/paciente.html';
            } else {
                messageDiv.textContent = 'Credenciales incorrectas';
            }
        } catch (error) {
            messageDiv.textContent = error.message;
        }
    });
});
    </script>
</body>
</html>