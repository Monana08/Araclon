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

@WebServlet("/RegistrarPaciente")
public class RegistroPaciente extends HttpServlet{
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
        String fechaNacimiento = request.getParameter("fecha_nacimiento");
        String genero = request.getParameter("genero");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String tipoSangre = request.getParameter("tipo_sangre");
        String alergias = request.getParameter("alergias");
        String enfermedadesPrevias = request.getParameter("enfermedades_previas");
        String contactoEmergenciaNombre = request.getParameter("contacto_emergencia_nombre");
        String contactoEmergenciaTelefono = request.getParameter("contacto_emergencia_telefono");
        String contactoEmergenciaRelacion = request.getParameter("contacto_emergencia_relacion");
        String seguroMedico = request.getParameter("seguro_medico");
        String numeroPoliza = request.getParameter("numero_poliza");
        String comentariosAdicionales = request.getParameter("comentarios_adicionales");

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            // Establecer conexión con la base de datos
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Consulta SQL para insertar los datos
            String sql = "INSERT INTO registropaciente (nombre, apellido, fecha_nacimiento, genero, direccion, telefono, email, tipo_sangre, alergias, enfermedades_previas, contacto_emergencia_nombre, contacto_emergencia_telefono, contacto_emergencia_relacion, seguro_medico, numero_poliza, comentarios_adicionales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Crear PreparedStatement
            pst = conn.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, apellido);
            pst.setDate(3, java.sql.Date.valueOf(fechaNacimiento)); // Convierte la fecha al formato SQL
            pst.setString(4, genero);
            pst.setString(5, direccion);
            pst.setString(6, telefono);
            pst.setString(7, email);
            pst.setString(8, tipoSangre);
            pst.setString(9, alergias);
            pst.setString(10, enfermedadesPrevias);
            pst.setString(11, contactoEmergenciaNombre);
            pst.setString(12, contactoEmergenciaTelefono);
            pst.setString(13, contactoEmergenciaRelacion);
            pst.setString(14, seguroMedico);
            pst.setString(15, numeroPoliza);
            pst.setString(16, comentariosAdicionales);

            // Ejecutar la consulta
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                response.getWriter().println("Paciente registrado exitosamente.");
            } else {
                response.getWriter().println("Error al registrar el paciente.");
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