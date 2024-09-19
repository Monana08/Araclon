package ficheros.controller;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/RecuperarPasswordServlet")
public class ResetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        // Configuración de la base de datos
        String jdbcURL = "jdbc:mysql://localhost/araclon?serverTimezone=America/Bogota";
        String jdbcUsername = "root";
        String jdbcPassword = "";
        try {
            // Registrar el controlador JDBC (si es necesario)
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {
                // Verificar si el correo electrónico está registrado
                String checkEmailSQL = "SELECT email FROM users2 WHERE email = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(checkEmailSQL)) {
                    pstmt.setString(1, email);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Generar un token de restablecimiento
                            String token = java.util.UUID.randomUUID().toString();
                            Timestamp expiry = new Timestamp(System.currentTimeMillis() + 3600000); // 1 hora
                            // Guardar el token en la base de datos
                            String updateTokenSQL = "UPDATE users2 SET reset_token = ?, reset_token_expiry = ? WHERE email = ?";
                            try (PreparedStatement pstmtUpdate = connection.prepareStatement(updateTokenSQL)) {
                                pstmtUpdate.setString(1, token);
                                pstmtUpdate.setTimestamp(2, expiry);
                                pstmtUpdate.setString(3, email);
                                pstmtUpdate.executeUpdate();
                            }
                            // Enviar correo electrónico
                            String subject = "Solicitud de restablecimiento de contraseña";
                            String body = "Para restablecer su contraseña, haga clic en el siguiente enlace: " +
                                    "<a href='http://localhost:8080/Araclon/resetPassword?token=" + token + "'>Restablecer contraseña</a>";
                            Properties props = new Properties();
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.port", "587");
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            Session mailSession = Session.getInstance(props, new Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication("hernandezmonana08@gmail.com", "OTHV SYTA JXZQ HAPQ");  // Cambia esto si usas contraseña específica para la aplicación
                                }
                            });
                            try {
                                Message message = new MimeMessage(mailSession);
                                message.setFrom(new InternetAddress("hernandezmonana08@gmail.com"));
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                                message.setSubject(subject);
                                message.setContent(body, "text/html");
                                Transport.send(message);
                                response.getWriter().println("El correo de restablecimiento de contraseña ha sido enviado.");
                            } catch (MessagingException e) {
                                e.printStackTrace();
                                response.getWriter().println("Error al enviar el correo: " + e.getMessage());
                            }
                        } else {
                            response.getWriter().println("El correo electrónico no está registrado.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Error al acceder a la base de datos: " + e.getMessage());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().println("Error al conectar con la base de datos: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Controlador JDBC no encontrado: " + e.getMessage());
        }
    }
}
 