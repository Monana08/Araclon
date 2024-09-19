package ficheros.controller;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/resetPassword")
public class PasswordResetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost/araclon?serverTimezone=America/Bogota";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            response.getWriter().println("Token de restablecimiento no proporcionado.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)) {
                String selectTokenSQL = "SELECT email FROM users2 WHERE reset_token = ? AND reset_token_expiry > NOW()";
                try (PreparedStatement pstmt = connection.prepareStatement(selectTokenSQL)) {
                    pstmt.setString(1, token);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Token válido, mostrar formulario
                            request.setAttribute("token", token);
                            request.getRequestDispatcher("/resetPasswordForm.jsp").forward(request, response);
                        } else {
                            response.getWriter().println("El enlace de restablecimiento de contraseña es inválido o ha expirado.");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            response.getWriter().println("Error en la carga del controlador JDBC: " + e.getMessage());
        } catch (SQLException e) {
            response.getWriter().println("Error en la base de datos: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            response.getWriter().println("Token o nueva contraseña no proporcionados.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)) {
                // Verificar si el token es válido y obtener el ID del usuario
                String selectTokenSQL = "SELECT id FROM users2 WHERE reset_token = ? AND reset_token_expiry > NOW()";
                try (PreparedStatement pstmt = connection.prepareStatement(selectTokenSQL)) {
                    pstmt.setString(1, token);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            int userId = rs.getInt("id");

                            // Actualizar la contraseña y limpiar el token
                            String updatePasswordSQL = "UPDATE users2 SET password = ?, reset_token = NULL, reset_token_expiry = NULL WHERE id = ?";
                            try (PreparedStatement pstmtUpdate = connection.prepareStatement(updatePasswordSQL)) {
                                pstmtUpdate.setString(1, newPassword); // Se almacena la contraseña tal cual
                                pstmtUpdate.setInt(2, userId);

                                int rowsAffected = pstmtUpdate.executeUpdate();

                                if (rowsAffected > 0) {
                                    response.getWriter().println("La contraseña ha sido restablecida con éxito.");
                                } else {
                                    response.getWriter().println("No se pudo restablecer la contraseña.");
                                }
                            }
                        } else {
                            response.getWriter().println("El enlace de restablecimiento de contraseña es inválido o ha expirado.");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            response.getWriter().println("Error en la carga del controlador JDBC: " + e.getMessage());
        } catch (SQLException e) {
            response.getWriter().println("Error en la base de datos: " + e.getMessage());
        }
    }
}
