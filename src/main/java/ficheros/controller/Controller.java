package ficheros.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/intro")
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Método GET: verifica si el usuario está autenticado
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Verifica si hay una sesión activa
        HttpSession session = request.getSession(false); // No crear una nueva sesión si no existe
        if (session != null && session.getAttribute("email") != null) {
            // Usuario autenticado
            PrintWriter out = response.getWriter();
            out.println("<h2>Bienvenido, " + session.getAttribute("email") + "</h2>");
            out.println("<a href='logout'>Cerrar sesión</a>");
        } else {
            // Usuario no autenticado, redirigir a la página de inicio de sesión
            response.sendRedirect("lgesp.html");
        }
    }

    // Método POST: maneja el inicio de sesión
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Obtener los parámetros del formulario
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Conectar a la base de datos y validar las credenciales
        boolean isValidUser = validateUser(email, password);

        if (isValidUser) {
            // Credenciales correctas, crear sesión
            HttpSession session = request.getSession();
            session.setAttribute("email", email);

            // Redirigir al usuario a la página de bienvenida o dashboard
            PrintWriter out = response.getWriter();
            out.println("<h2>Bienvenid@, " + session.getAttribute("email") + "</h2>");
          response.sendRedirect("Secretaria.html");
        } else {
            // Credenciales incorrectas, mostrar error
            PrintWriter out = response.getWriter();
            out.println("<h3>Usuario o contraseña incorrectos</h3>");
            request.getRequestDispatcher("lgesp.html").include(request, response);
        }
    }

    // Método para validar las credenciales del usuario
    private boolean validateUser(String email, String password) {
        boolean isValid = false;
        String dbURL = "jdbc:mysql://localhost:3306/araclon?serverTimezone=America/Bogota"; // Cambia esto a tu URL de la base de datos
        String dbUser = "root"; // Cambia esto a tu usuario de la base de datos
        String dbPassword = ""; // Cambia esto a tu contraseña de la base de datos

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Asegúrate de tener el conector MySQL en tu proyecto
            Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);

            String sql = "SELECT * FROM users2 WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isValid = true; // Usuario encontrado
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }
}


