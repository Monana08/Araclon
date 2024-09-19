package ficheros.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegistrarEnfermero")
public class RegistroEnfermero extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // URL, usuario y contraseña de la base de datos
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/araclon?serverTimezone=America/Bogota";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener los parámetros del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String fechaNacimiento = request.getParameter("fecha_nacimiento");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String direccion = request.getParameter("direccion");
        String especialidad = request.getParameter("especialidad");
        String fechaIngreso = request.getParameter("fecha_ingreso");
        String comentariosAdicionales = request.getParameter("comentarios_adicionales");

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            // Establecer conexión con la base de datos
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Consulta SQL para insertar los datos en la tabla "enfermeros"
            String sql = "INSERT INTO enfermeros (nombre, apellido, dni, fecha_nacimiento, telefono, email, direccion, especialidad, fecha_ingreso, comentarios_adicionales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Crear PreparedStatement
            pst = conn.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, apellido);
            pst.setString(3, dni);
            pst.setDate(4, java.sql.Date.valueOf(fechaNacimiento)); // Convierte la fecha al formato SQL
            pst.setString(5, telefono);
            pst.setString(6, email);
            pst.setString(7, direccion);
            pst.setString(8, especialidad);
            pst.setDate(9, java.sql.Date.valueOf(fechaIngreso)); // Convierte la fecha de ingreso al formato SQL
            pst.setString(10, comentariosAdicionales);

            // Ejecutar la consulta
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                response.getWriter().println("Enfermero registrado exitosamente.");
            } else {
                response.getWriter().println("Error al registrar el enfermero.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error al procesar la solicitud.");
        } finally {
            // Cerrar recursos
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}